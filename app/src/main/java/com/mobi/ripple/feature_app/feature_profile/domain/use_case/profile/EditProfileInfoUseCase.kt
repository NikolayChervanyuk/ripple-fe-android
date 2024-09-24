package com.mobi.ripple.feature_app.feature_profile.domain.use_case.profile

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.profile.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.repository.PersonalProfileRepository

class EditProfileInfoUseCase(
    val repository: PersonalProfileRepository
) {
    suspend operator fun invoke(userProfileInfo: UserProfileInfo): Response<Boolean?> {
        return repository.editProfileInfo(userProfileInfo)
    }
}