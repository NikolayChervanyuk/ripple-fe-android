package com.mobi.ripple.feature_app.feature_profile.domain.use_case

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class GetProfileInfoUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): Response<UserProfileInfo?> {
        return repository.getUserProfileInfo(username)
    }
}