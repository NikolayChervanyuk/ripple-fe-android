package com.mobi.ripple.core.data.followers_following.data_source.remote

import com.mobi.ripple.core.config.AppUrls
import com.mobi.ripple.core.data.followers_following.data_source.remote.dto.FollowersFollowingSimpleUserResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiRequest
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class FollowersFollowingApiServiceImpl(
    val client: HttpClient
) : FollowersFollowingApiService {
    override suspend fun getFollowers(
        username: String,
        page: Int
    ): ApiResponse<List<FollowersFollowingSimpleUserResponse>> =
        ApiRequest<List<FollowersFollowingSimpleUserResponse>> {
            client.get(AppUrls.ProfileUrls.getFollowers(username, page))
        }.sendRequest()

    override suspend fun getFollowing(
        username: String,
        page: Int
    ): ApiResponse<List<FollowersFollowingSimpleUserResponse>> =
        ApiRequest<List<FollowersFollowingSimpleUserResponse>> {
            client.get(AppUrls.ProfileUrls.getFollowing(username, page))
        }.sendRequest()
}