package com.mobi.ripple.core.domain.repository

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.followers_following.FollowersFollowingSimpleUser

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