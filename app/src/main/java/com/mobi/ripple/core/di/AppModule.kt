package com.mobi.ripple.core.di

import android.content.Context
import arrow.core.Either
import com.mobi.ripple.RootAppManager
import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.util.toNetworkError
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.RefreshTokenRequest
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.RefreshTokenResponse
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRootAppManager(@ApplicationContext context: Context): RootAppManager {
        return RootAppManager(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
      return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideHttpClient(rootAppManager: RootAppManager): HttpClient {
        return HttpClient(Android) {
            expectSuccess = false
            defaultRequest {
                url(AppUrls.BASE_URL)
                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = false
//                        explicitNulls = false
                        prettyPrint = false
                        isLenient = true
                    }
                )
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val authTokens = rootAppManager.getStoredAuthTokens()
                        if (authTokens != null) {
                            return@loadTokens BearerTokens(
                                authTokens.accessToken,
                                authTokens.refreshToken
                            )
                        }
                        return@loadTokens null
                    }
                    refreshTokens {
                        val tokens =
                            rootAppManager.getStoredAuthTokens() ?: return@refreshTokens null
                        val response = Either.catch {
                            client.post {
                                url(AppUrls.AuthUrls.REFRESH_TOKENS_URL)
                                setBody(RefreshTokenRequest(tokens.refreshToken))
                            }.body<RefreshTokenResponse>()
                        }.mapLeft { it.toNetworkError() }
                        response.onRight {
                            rootAppManager.saveAuthTokens(
                                AuthTokens(
                                    it.accessToken,
                                    it.refreshToken
                                )
                            )
                            return@refreshTokens BearerTokens(it.accessToken, it.refreshToken)
                        }
                        return@refreshTokens null
                    }
                }
            }
            install(Logging) {
                logger = Logger.DEFAULT
            }
            this.developmentMode = true
        }
    }
}