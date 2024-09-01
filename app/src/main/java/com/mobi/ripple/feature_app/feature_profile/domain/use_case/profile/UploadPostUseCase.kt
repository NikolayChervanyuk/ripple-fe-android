package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class UploadPostUseCase(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(image: ByteArray): Response<Boolean?> {
        TODO("Not implemented")
        //return repository.uploadUserProfilePicture(image)
    }
}