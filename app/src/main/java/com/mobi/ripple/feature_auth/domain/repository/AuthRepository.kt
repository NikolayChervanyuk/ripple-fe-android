package com.mobi.ripple.feature_auth.domain.repository

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import com.mobi.ripple.feature_auth.domain.model.UserLogin
import com.mobi.ripple.feature_auth.domain.model.UserRegister

interface AuthRepository {

    suspend fun authenticate(userLogin: UserLogin): Response<AuthTokens>
    suspend fun register(userRegister: UserRegister): Response<Boolean>

    suspend fun isUsernameTaken(username: String): Response<Boolean>
    suspend fun isEmailTaken(email: String): Response<Boolean>
}