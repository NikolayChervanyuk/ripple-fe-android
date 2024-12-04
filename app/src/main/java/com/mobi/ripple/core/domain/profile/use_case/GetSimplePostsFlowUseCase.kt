package com.mobi.ripple.core.domain.profile.use_case

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.profile.model.UserProfileSimplePost
import com.mobi.ripple.core.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetSimplePostsFlowUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        username: String
    ): Flow<PagingData<UserProfileSimplePost>> {
        return repository.getSimpleUserPostsFlow(username)
    }
}