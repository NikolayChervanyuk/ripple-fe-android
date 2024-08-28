package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.data.ApiResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserProfileInfoResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserProfilePictureResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserSimplePostsResponse
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture

interface ProfileApiService {
    suspend fun getUserProfilePicture(username: String): ApiResponse<ByteArray>
    suspend fun getUserProfileInfo(username: String): ApiResponse<UserProfileInfoResponse>
    suspend fun getSimplePosts(
        username: String,
        page: Long,
    ): ApiResponse<UserSimplePostsResponse>
}