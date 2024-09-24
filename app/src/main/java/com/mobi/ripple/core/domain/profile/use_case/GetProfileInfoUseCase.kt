package com.mobi.ripple.core.domain.profile.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.profile.model.UserProfileInfo
import com.mobi.ripple.core.domain.profile.repository.ProfileRepository

class GetProfileInfoUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): Response<UserProfileInfo?> {
        return repository.getUserProfileInfo(username)
    }
}