package com.mobi.ripple.core.domain.use_case.profile

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.ProfileRepository

class ChangeFollowingStateUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(username: String): Response<Boolean?> {
        return repository.changeFollowingState(username)
    }
}