package com.mobi.ripple.core.data.data_source.remote.followers_following

import com.mobi.ripple.core.data.data_source.remote.followers_following.dto.FollowersFollowingSimpleUserResponse
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse

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