package com.mobi.ripple.feature_app.feature_profile.domain.use_case

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetSimplePostsUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        username: String,
        page: Long
    ): Response<List<UserProfileSimplePost>> { //JWT token
        return repository.getSimpleUserPosts(username, page)
    }
}