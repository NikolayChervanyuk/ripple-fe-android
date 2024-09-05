package com.mobi.ripple.feature_auth.domain.use_case

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import com.mobi.ripple.feature_auth.domain.model.UserLogin
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(userLogin: UserLogin): Response<AuthTokens> {
        return repository.authenticate(userLogin)
    }
}