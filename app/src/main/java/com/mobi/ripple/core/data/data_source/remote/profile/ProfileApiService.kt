package com.mobi.ripple.core.data.data_source.remote.profile

import com.mobi.ripple.core.data.data_source.remote.profile.dto.UserProfileInfoResponse
import com.mobi.ripple.core.data.data_source.remote.profile.dto.UserSimplePostsResponse
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse

interface ProfileApiService {
    suspend fun getUserProfilePicture(username: String): ApiResponse<String>
    suspend fun getUserProfileInfo(username: String): ApiResponse<UserProfileInfoResponse>
    suspend fun getSimplePosts(
        username: String,
        page: Long,
    ): ApiResponse<List<UserSimplePostsResponse>>
}