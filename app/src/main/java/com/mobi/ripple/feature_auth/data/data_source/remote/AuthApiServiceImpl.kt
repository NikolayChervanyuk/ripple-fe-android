package com.mobi.ripple.feature_auth.data.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import com.mobi.ripple.core.util.invalidateBearerTokens
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.LoginRequest
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.LoginResponse
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.RegisterRequest
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.SimpleAuthUserResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class AuthApiServiceImpl @Inject constructor(
    private val client: HttpClient
) : AuthApiService {
    override suspend fun authenticate(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        return ApiRequest<LoginResponse> {
            client.post(AppUrls.AuthUrls.LOGIN_URL) {
                setBody(loginRequest)
            }
        }.sendRequest()
    }

    override suspend fun register(registerRequest: RegisterRequest): ApiResponse<Boolean> {
        return ApiRequest<Boolean> {
            client.post(AppUrls.AuthUrls.REGISTER_URL) {
                setBody(registerRequest)
            }
        }.sendRequest()
    }

    override suspend fun isUsernameTaken(username: String): ApiResponse<Boolean> {

        return ApiRequest<Boolean> {
            client.get(AppUrls.AuthUrls.USERS) {
                parameter("username", username)
            }
        }.sendRequest<Boolean>()
    }

    override suspend fun isEmailTaken(email: String): ApiResponse<Boolean> {
        return ApiRequest<Boolean> {
            client.get(AppUrls.AuthUrls.USERS) {
                parameter("email", email)
            }
        }.sendRequest()
    }

    override suspend fun getSimpleAuthUser(
        username: String,
        shouldInvalidateTokens: Boolean
    ): ApiResponse<SimpleAuthUserResponse> {
        if (shouldInvalidateTokens) client.invalidateBearerTokens()
        return ApiRequest<SimpleAuthUserResponse> {
            client.get(AppUrls.AuthUrls.getSimpleAuthUser(username))
        }.sendRequest()
    }
}