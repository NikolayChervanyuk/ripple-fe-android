package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class IsOtherUserWithUsernameExistsUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): Response<Boolean?> {
        return repository.isOtherUserWithUsernameExists(username)
    }
}