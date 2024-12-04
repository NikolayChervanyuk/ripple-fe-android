package com.mobi.ripple.core.data.profile.data_source.remote

import com.mobi.ripple.core.data.profile.data_source.remote.dto.UserProfileInfoResponse
import com.mobi.ripple.core.data.profile.data_source.remote.dto.UserSimplePostsResponse
import com.mobi.ripple.core.data.common.data_source.wrappers.ApiResponse

interface ProfileApiService {
    suspend fun getUserProfilePicture(username: String): ApiResponse<String>
    suspend fun getUserProfileInfo(username: String): ApiResponse<UserProfileInfoResponse>
    suspend fun getSimplePosts(
        username: String,
        page: Long,
    ): ApiResponse<List<UserSimplePostsResponse>>
    suspend fun changeFollowingState(username: String): ApiResponse<Boolean>
}
