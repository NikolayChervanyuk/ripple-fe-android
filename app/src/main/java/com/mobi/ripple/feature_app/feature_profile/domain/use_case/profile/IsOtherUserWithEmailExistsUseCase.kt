package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class IsOtherUserWithEmailExistsUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(email: String): Response<Boolean?> {
        return repository.isOtherUserWithEmailExists(email)
    }
}