package com.mobi.ripple.feature_auth.data.data_source.remote


import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.LoginRequest
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.LoginResponse
import com.mobi.ripple.feature_auth.data.data_source.remote.dto.RegisterRequest

interface AuthApiService {

    suspend fun authenticate(loginRequest: LoginRequest): ApiResponse<LoginResponse>

    suspend fun register(registerRequest: RegisterRequest): ApiResponse<Boolean>

    suspend fun isUsernameTaken(username: String): ApiResponse<Boolean>
    suspend fun isEmailTaken(email: String): ApiResponse<Boolean>
}