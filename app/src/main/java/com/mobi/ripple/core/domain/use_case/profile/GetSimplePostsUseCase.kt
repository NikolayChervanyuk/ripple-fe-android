package com.mobi.ripple.core.domain.use_case.profile

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import com.mobi.ripple.core.domain.repository.ProfileRepository

class GetSimplePostsUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        username: String,
        page: Long
    ): Response<List<UserProfileSimplePost>> {
        return repository.getSimpleUserPosts(username, page)
    }
}