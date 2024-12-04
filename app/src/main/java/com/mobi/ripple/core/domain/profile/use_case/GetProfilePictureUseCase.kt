package com.mobi.ripple.core.domain.profile.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.profile.model.UserProfilePicture
import com.mobi.ripple.core.domain.profile.repository.ProfileRepository

class GetProfilePictureUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): Response<UserProfilePicture?> {
        return repository.getUserProfilePicture(username)
    }
}