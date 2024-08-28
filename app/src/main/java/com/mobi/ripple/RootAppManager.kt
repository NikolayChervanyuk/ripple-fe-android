package com.mobi.ripple

import android.content.Context
import android.util.ArrayMap
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mobi.ripple.core.exceptions.JwtClaimNotFoundException
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RootAppManager @Inject constructor(
    context: Context
) {
    private val dataStoreRepository: DataStoreRepository = DataStoreRepository(context)

    var isUserHavingAuthTokens: Boolean = false
        private set

    var storedUsername: String? = null
        private set

    private var authTokens: AuthTokens? = null

    private val _eventFlow = MutableSharedFlow<RootUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        runBlocking {
            val tokens = dataStoreRepository.getStoredAuthTokens()
            val username = dataStoreRepository.getStoredUsername()
            if (tokens != null && username != null) {
                isUserHavingAuthTokens = true
                authTokens = tokens
                storedUsername = username
            } else _eventFlow.emit(RootUiEvent.LogOut)
        }
    }

    fun getStoredAuthTokens(): AuthTokens? {
        return authTokens
    }

    suspend fun saveAuthTokens(authTokens: AuthTokens): Boolean {
        if (dataStoreRepository.saveTokens(authTokens)) {
            this.authTokens = authTokens
            storedUsername = dataStoreRepository.getStoredUsername()
            return true
        }
        return false
    }

    private suspend fun clearAuthTokensAndUsername(): Boolean {
        if (dataStoreRepository.clearTokensAndUsername()) {
            authTokens = null
            storedUsername = null
            return true
        }
        return false
    }

    fun onSuccessfulLogin(authTokens: AuthTokens) {
        runBlocking {
            launch {
                saveAuthTokens(authTokens)
                _eventFlow.emit(RootUiEvent.LogIn)
            }
        }
    }

    fun onLogout() {
        runBlocking {
            launch {
                clearAuthTokensAndUsername()
            }
            launch {
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
                Log.e("DataStore", "Can't read data from disk")
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
                Log.e(
                    "DataStore",
                    "Cannot save authToken, writing data to disk failed. Maybe there is no free space left?"
                )
            } catch (e: JwtClaimNotFoundException) {
                Log.e(
                    "DataStore",
                    "Cannot find sub claim (the user) in the JWT token, but is required."
                )
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
                Log.e("DataStore", "Can't write data to disk")
            }
            return false
        }

        suspend fun getStoredUsername(): String? {
            val usernameKey = stringPreferencesKey(DataStoreKeys.USERNAME.keyName)
            try {
                val preferences: Preferences = context.dataStore.data.first()
                return preferences[usernameKey]
            } catch (e: IOException) {
                Log.e("DataStore", "Can't read data from disk")
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
                Log.e("DataStore", "Can't write data to disk")
            }
            return false
        }

        suspend fun clearAllPreferences(): Boolean {
            try {
                context.dataStore.edit { it.clear() }
                return true
            } catch (e: IOException) {
                Log.e("DataStore", "Can't write data to disk")
            }
            return false
        }

        private enum class DataStoreKeys(val keyName: String) {
            REFRESH_TOKEN("refresh_token"),
            ACCESS_TOKEN("access_token"),
            USERNAME("username")
        }
    }
}

