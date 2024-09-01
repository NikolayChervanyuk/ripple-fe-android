package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.data.ApiResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UpdateUserProfileInfoRequest
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserProfileInfoResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UserSimplePostsResponse

interface ProfileApiService {
    suspend fun getUserProfilePicture(username: String): ApiResponse<String>
    suspend fun uploadUserProfilePicture(image: ByteArray): ApiResponse<Boolean>
    suspend fun deleteUserProfilePicture(): ApiResponse<Boolean>

    suspend fun getUserProfileInfo(username: String): ApiResponse<UserProfileInfoResponse>

    suspend fun isOtherUserWithUsernameExists(username: String): ApiResponse<Boolean>
    suspend fun isOtherUserWithEmailExists(email: String): ApiResponse<Boolean>
    suspend fun editProfileInfo(
        updateUserProfileInfoRequest: UpdateUserProfileInfoRequest
    ): ApiResponse<Boolean>

    suspend fun getSimplePosts(
        username: String,
        page: Long,
    ): ApiResponse<List<UserSimplePostsResponse>>
}