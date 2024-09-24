package com.mobi.ripple.feature_auth.data.repository

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_auth.data.data_source.remote.AuthApiService
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.asLoginRequest
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.asRegisterRequest
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import com.mobi.ripple.feature_auth.domain.model.UserLogin
import com.mobi.ripple.feature_auth.domain.model.UserRegister
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {
    override suspend fun authenticate(userLogin: UserLogin): Response<AuthTokens> {
        val apiResponse = authApiService.authenticate(userLogin.asLoginRequest())

        return apiResponse.toResponse(apiResponse.content?.asAuthTokens())
    }

    override suspend fun register(userRegister: UserRegister): Response<Boolean> {
        val apiResponse = authApiService.register(userRegister.asRegisterRequest())

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun isUsernameTaken(username: String): Response<Boolean> {
        val apiResponse = authApiService.isUsernameTaken(username)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun isEmailTaken(email: String): Response<Boolean> {
        val apiResponse = authApiService.isEmailTaken(email)

        return apiResponse.toResponse(apiResponse.content)
    }
}