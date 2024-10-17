package com.mobi.ripple

import android.content.Context
import android.util.ArrayMap
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobi.ripple.core.exceptions.JwtClaimNotFoundException
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import io.jsonwebtoken.Jwts
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import java.util.Base64
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RootAppManager @Inject constructor(
    context: Context
) {
    var isChatOpened = mutableStateOf(false)
    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(context)

    var isUserHavingAuthTokens: Boolean = false
        private set

    var storedUsername: String? = null
        private set

    var storedId: String? = null
        private set

    private var authTokens: AuthTokens? = null

    private val _eventFlow = MutableSharedFlow<RootUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        runBlocking {
            val tokens = dataStoreRepository.getStoredAuthTokens()
            val username = dataStoreRepository.getStoredUsername()
            val id = dataStoreRepository.getStoredId()
            if (tokens != null && username != null) {
                isUserHavingAuthTokens = true
                authTokens = tokens
                storedUsername = username
                storedId = id
            } else _eventFlow.emit(RootUiEvent.LogOut)
        }
    }

    suspend fun getProfilePicture(): ByteArray? {
        return dataStoreRepository.getProfilePicture()
    }

    suspend fun storeProfilePicture(imageBytes: ByteArray) {
        dataStoreRepository.saveProfilePicture(imageBytes)
    }

    suspend fun deleteProfilePicture() {
        dataStoreRepository.deleteProfilePicture()
    }

    suspend fun getSmallProfilePicture(): ByteArray? {
        return dataStoreRepository.getSmallProfilePicture()
    }

    suspend fun storeSmallProfilePicture(imageBytes: ByteArray) {
        dataStoreRepository.saveSmallProfilePicture(imageBytes)
    }

    suspend fun storeId(userId: String) {
        dataStoreRepository.saveId(userId)
        storedId = userId
    }

    fun getStoredAuthTokens(): AuthTokens? {
        return authTokens
    }


    suspend fun saveAuthTokens(authTokens: AuthTokens): Boolean {
        var storeSuccessful: Boolean
        runBlocking { storeSuccessful = dataStoreRepository.saveTokens(authTokens) }
        this.authTokens = authTokens
        isUserHavingAuthTokens = true
        storedUsername = dataStoreRepository.getStoredUsername()
        return storeSuccessful
    }

    private suspend fun clearAuthTokensAndUsername(): Boolean {
        if (dataStoreRepository.clearTokensAndUsername()) {
            authTokens = null
            storedUsername = null
            isUserHavingAuthTokens = false
            return true
        }
        return false
    }

    suspend fun storeAuthTokens(authTokens: AuthTokens) {
        coroutineScope {
            launch {
                saveAuthTokens(authTokens)
            }
        }
    }

    suspend fun onSuccessfulLogin() {
        coroutineScope {
            launch {
                _eventFlow.emit(RootUiEvent.LogIn)
            }
        }
    }

    suspend fun onLogout(routeToLoginPage: Boolean = true) {
        coroutineScope {
            launch {
                clearAuthTokensAndUsername()
            }
            if (routeToLoginPage) {
                _eventFlow.emit(RootUiEvent.LogOut)
            }
        }
    }

    sealed class RootUiEvent {
        data object LogIn : RootUiEvent()
        data object LogOut : RootUiEvent()
    }

    private class DataStoreRepository(private val context: Context) {
        suspend fun getStoredAuthTokens(): AuthTokens? {
            val refreshTokenKey = stringPreferencesKey(DataStoreKeys.REFRESH_TOKEN.keyName)
            val accessTokenKey = stringPreferencesKey(DataStoreKeys.ACCESS_TOKEN.keyName)
            try {
                val preferences: Preferences = context.dataStore.data.first()
                val accessToken = preferences[accessTokenKey]
                val refreshToken = preferences[refreshTokenKey]

                if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                    return AuthTokens(accessToken, refreshToken)
                }
            } catch (e: IOException) {
                Timber.tag("DataStore").e("Can't read data from disk")
            }
            return null
        }

        /**
         * Saves access and refresh tokens in the [DataStore] and
         * tries to extract and save the username from the `sub` (the username) claim.
         * Make sure at least one of the tokens contains `sub` claim or else saving will fail.
         * @return `true` if saving is successful, otherwise `false`
         * @throws [JwtClaimNotFoundException] if no `sub` claim is found
         */
        suspend fun saveTokens(newTokens: AuthTokens): Boolean {
            try {
                if (!storeUsernameFromJwtToken(newTokens.refreshToken)) {
                    if (!storeUsernameFromJwtToken(newTokens.accessToken)) {
                        throw JwtClaimNotFoundException()
                    }
                }
                val refreshTokenKey = stringPreferencesKey(DataStoreKeys.REFRESH_TOKEN.keyName)
                val accessTokenKey = stringPreferencesKey(DataStoreKeys.ACCESS_TOKEN.keyName)
                context.dataStore.edit { settings ->
                    settings[accessTokenKey] = newTokens.accessToken
                    settings[refreshTokenKey] = newTokens.refreshToken
                }
                return true
            } catch (e: IOException) {
                Timber.tag("DataStore")
                    .e("Cannot save authToken, writing data to disk failed. Maybe there is no free space left?")
            } catch (e: JwtClaimNotFoundException) {
                Timber.tag("DataStore")
                    .e("Cannot find sub claim (the user) in the JWT token, but is required.")
            }
            return false
        }

        //TODO: Currently not using signature to trust the token, this makes the app
        // vulnerable to Denial of service attacks by man-in-the-middle
        // when resource requires this username.
        private suspend fun storeUsernameFromJwtToken(jwtToken: String): Boolean {
            val payload = jwtToken.substringAfter('.').substringBeforeLast('.')
            val decodedBody = String(Base64.getDecoder().decode(payload))
            val claimsMapJson = Json.parseToJsonElement(decodedBody).jsonObject.toMap()
            val claimsMap = ArrayMap<String, String>()
            for (claim in claimsMapJson) {
                claimsMap[claim.key] = claim.value.jsonPrimitive.content
            }
            val claims = Jwts
                .claims()
                .add(claimsMap)
                .build()
            val username: String? = claims.subject
            if (username.isNullOrEmpty()) return false

            val usernameKey = stringPreferencesKey(DataStoreKeys.USERNAME.keyName)
            try {
                context.dataStore.edit { settings ->
                    settings[usernameKey] = username
                }
                return true
            } catch (e: IOException) {
                Timber.tag("DataStore").e("Can't write data to disk")
            }
            return false
        }

        suspend fun getStoredUsername(): String? {
            val usernameKey = stringPreferencesKey(DataStoreKeys.USERNAME.keyName)
            try {
                val preferences: Preferences = context.dataStore.data.first()
                return preferences[usernameKey]
            } catch (e: IOException) {
                Timber.tag("DataStore").e("Can't read data from disk")
            }
            return null
        }

        suspend fun clearTokensAndUsername(): Boolean {
            val refreshTokenKey = stringPreferencesKey(DataStoreKeys.REFRESH_TOKEN.keyName)
            val accessTokenKey = stringPreferencesKey(DataStoreKeys.ACCESS_TOKEN.keyName)
            val usernameKey = stringPreferencesKey(DataStoreKeys.USERNAME.keyName)
            try {
                context.dataStore.edit {
                    it.remove(refreshTokenKey)
                    it.remove(accessTokenKey)
                    it.remove(usernameKey)
                }
                return true
            } catch (e: IOException) {
                Timber.tag("DataStore").e("Can't write data to disk")
            }
            return false
        }

        suspend fun clearAllPreferences(): Boolean {
            try {
                context.dataStore.edit { it.clear() }
                return true
            } catch (e: IOException) {
                Timber.tag("DataStore").e("Can't write data to disk")
            }
            return false
        }

        suspend fun saveProfilePicture(imageBytes: ByteArray) {
            val profilePictureKey = stringPreferencesKey(DataStoreKeys.PROFILE_PICTURE.keyName)
            context.dataStore.edit {
                it[profilePictureKey] = imageBytes.encodeBase64()
            }
        }

        suspend fun getProfilePicture(): ByteArray? {
            val profilePictureKey = stringPreferencesKey(DataStoreKeys.PROFILE_PICTURE.keyName)
            val preferences: Preferences = context.dataStore.data.first()
            return preferences[profilePictureKey]?.decodeBase64Bytes()
        }

        suspend fun saveSmallProfilePicture(imageBytes: ByteArray) {
            val profilePictureKey =
                stringPreferencesKey(DataStoreKeys.SMALL_PROFILE_PICTURE.keyName)
            context.dataStore.edit {
                it[profilePictureKey] = imageBytes.encodeBase64()
            }
        }

        suspend fun getSmallProfilePicture(): ByteArray? {
            val profilePictureKey =
                stringPreferencesKey(DataStoreKeys.SMALL_PROFILE_PICTURE.keyName)
            val preferences: Preferences = context.dataStore.data.first()
            return preferences[profilePictureKey]?.decodeBase64Bytes()
        }

        suspend fun deleteProfilePicture() {
            val profilePictureKey = stringPreferencesKey(DataStoreKeys.PROFILE_PICTURE.keyName)
            context.dataStore.edit {
                it.remove(profilePictureKey)
            }
        }

        suspend fun getStoredId(): String? {
            val usernameKey = stringPreferencesKey(DataStoreKeys.ID.keyName)
            try {
                val preferences: Preferences = context.dataStore.data.first()
                return preferences[usernameKey]
            } catch (e: IOException) {
                Timber.tag("DataStore").e("Can't read data from disk")
            }
            return null
        }


        suspend fun saveId(id: String) {
            val idKey = stringPreferencesKey(DataStoreKeys.ID.keyName)
            context.dataStore.edit {
                it[idKey] = id
            }
        }

        private enum class DataStoreKeys(val keyName: String) {
            REFRESH_TOKEN("refresh_token"),
            ACCESS_TOKEN("access_token"),
            USERNAME("username"),
            ID("userId"),
            PROFILE_PICTURE("pfp"),
            SMALL_PROFILE_PICTURE("small_pfp")
        }
    }
}

