package com.mobi.ripple.core.data.followers_following.data_source.remote

import com.mobi.ripple.core.data.followers_following.data_source.remote.dto.FollowersFollowingSimpleUserResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse

interface FollowersFollowingApiService {
    suspend fun getFollowers(
        username: String,
        page: Int
    ): ApiResponse<List<FollowersFollowingSimpleUserResponse>>

    suspend fun getFollowing(
        username: String,
        page: Int
    ): ApiResponse<List<FollowersFollowingSimpleUserResponse>>
}