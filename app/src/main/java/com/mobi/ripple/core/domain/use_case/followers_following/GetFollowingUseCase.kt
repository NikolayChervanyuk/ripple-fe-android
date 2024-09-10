package com.mobi.ripple.core.domain.use_case.followers_following

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.followers_following.FollowersFollowingSimpleUser
import com.mobi.ripple.core.domain.repository.FollowersFollowingRepository

class GetFollowingUseCase(
    private val repository: FollowersFollowingRepository
) {
    suspend operator fun invoke(username: String, page: Int): Response<List<FollowersFollowingSimpleUser>> {
        return repository.getFollowing(username, page)
    }
}