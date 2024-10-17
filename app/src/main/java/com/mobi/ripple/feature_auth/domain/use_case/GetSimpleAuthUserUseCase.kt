package com.mobi.ripple.feature_auth.domain.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_auth.domain.model.SimpleAuthUser
import com.mobi.ripple.feature_auth.domain.repository.AuthRepository

class GetSimpleAuthUserUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, shouldInvalidateTokens: Boolean): Response<SimpleAuthUser?> {
        return repository.getAuthUser(username, shouldInvalidateTokens)
    }
}