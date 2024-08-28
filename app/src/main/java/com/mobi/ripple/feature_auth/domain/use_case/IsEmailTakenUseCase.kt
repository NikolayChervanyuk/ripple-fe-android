package com.mobi.ripple.feature_auth.domain.use_case

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository

class IsEmailTakenUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Response<Boolean> {
        return repository.isEmailTaken(email)
    }
}