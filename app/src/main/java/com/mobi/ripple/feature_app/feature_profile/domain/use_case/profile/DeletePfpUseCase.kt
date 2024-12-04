package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_profile.domain.repository.PersonalProfileRepository

class DeletePfpUseCase(
    private val repository: PersonalProfileRepository
) {
    suspend operator fun invoke(): Response<Boolean?> {
        return repository.deleteUserProfilePicture()
    }
}