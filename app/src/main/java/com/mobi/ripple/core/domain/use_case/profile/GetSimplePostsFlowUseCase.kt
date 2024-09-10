package com.mobi.ripple.core.domain.use_case.profile

import androidx.paging.PagingData
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import com.mobi.ripple.core.domain.repository.ProfileRepository
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