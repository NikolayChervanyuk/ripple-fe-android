package com.mobi.ripple.core.data.repository

import com.mobi.ripple.core.data.data_source.remote.followers_following.FollowersFollowingApiService
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.model.followers_following.FollowersFollowingSimpleUser
import com.mobi.ripple.core.domain.repository.FollowersFollowingRepository

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