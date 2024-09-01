package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class EditProfileInfoUseCase(
    val repository: ProfileRepository
) {
    suspend operator fun invoke(userProfileInfo: UserProfileInfo): Response<Boolean?> {
        return repository.editProfileInfo(userProfileInfo)
    }
}