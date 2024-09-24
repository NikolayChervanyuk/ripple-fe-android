package com.mobi.ripple.core.data.followers_following.repository

import com.mobi.ripple.core.data.followers_following.data_source.remote.FollowersFollowingApiService
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.followers_following.model.FollowersFollowingSimpleUser
import com.mobi.ripple.core.domain.followers_following.repository.FollowersFollowingRepository

class FollowersFollowingRepositoryImpl(
    private val apiService: FollowersFollowingApiService
) : FollowersFollowingRepository {
    override suspend fun getFollowers(
        username: String,
        page: Int
    ): Response<List<FollowersFollowingSimpleUser>> {
        val apiResponse = apiService.getFollowers(username, page)

        return apiResponse.toResponse(
            apiResponse.content?.map {
                it.asFollowersFollowingSimpleUser()
            }
        )
    }

    override suspend fun getFollowing(
        username: String,
        page: Int
    ): Response<List<FollowersFollowingSimpleUser>> {
        val apiResponse = apiService.getFollowing(username, page)

        return apiResponse.toResponse(
            apiResponse.content?.map {
                it.asFollowersFollowingSimpleUser()
            }
        )
    }
}