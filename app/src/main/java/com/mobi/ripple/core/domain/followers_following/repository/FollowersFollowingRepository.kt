package com.mobi.ripple.core.domain.followers_following.repository

import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.followers_following.model.FollowersFollowingSimpleUser

interface FollowersFollowingRepository {
    suspend fun getFollowers(
        username: String,
        page: Int
    ): Response<List<FollowersFollowingSimpleUser>>

    suspend fun getFollowing(
        username: String,
        page: Int
    ): Response<List<FollowersFollowingSimpleUser>>
}