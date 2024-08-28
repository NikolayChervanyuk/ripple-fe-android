package com.mobi.ripple.feature_auth.domain.use_case

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_auth.domain.model.AuthTokens
import com.mobi.ripple.feature_auth.domain.model.UserRegister
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(userRegister: UserRegister): Response<Boolean> {
        return repository.register(userRegister)
    }
}