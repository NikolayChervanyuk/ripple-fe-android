package com.mobi.ripple.feature_auth.domain.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository

class IsUsernameTakenUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String): Response<Boolean> {
        return repository.isUsernameTaken(username)
    }
}