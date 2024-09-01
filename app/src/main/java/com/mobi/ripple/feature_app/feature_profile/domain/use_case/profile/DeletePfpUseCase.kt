package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class DeletePfpUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Response<Boolean?> {
        return repository.deleteUserProfilePicture()
    }
}