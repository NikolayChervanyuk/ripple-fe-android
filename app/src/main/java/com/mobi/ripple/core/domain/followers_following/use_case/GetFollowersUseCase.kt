package com.mobi.ripple.core.domain.followers_following.use_case

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.followers_following.model.FollowersFollowingSimpleUser
import com.mobi.ripple.core.domain.followers_following.repository.FollowersFollowingRepository

class GetFollowersUseCase(
    private val repository: FollowersFollowingRepository
) {
    suspend operator fun invoke(username: String, page: Int): Response<List<FollowersFollowingSimpleUser>> {
        return repository.getFollowers(username, page)
    }
}