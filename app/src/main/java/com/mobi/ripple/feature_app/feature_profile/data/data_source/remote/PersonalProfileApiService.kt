package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote

import com.mobi.ripple.core.data.data_source.remote.profile.ProfileApiService
import com.mobi.ripple.core.data.data_source.remote.wrappers.ApiResponse
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UpdateUserProfileInfoRequest
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.UploadNewPostRequest

interface PersonalProfileApiService : ProfileApiService {

    suspend fun uploadUserProfilePicture(image: ByteArray): ApiResponse<Boolean>

    suspend fun deleteUserProfilePicture(): ApiResponse<Boolean>
    suspend fun isOtherUserWithUsernameExists(username: String): ApiResponse<Boolean>
    suspend fun isOtherUserWithEmailExists(email: String): ApiResponse<Boolean>


    suspend fun editProfileInfo(
        updateUserProfileInfoRequest: UpdateUserProfileInfoRequest
    ): ApiResponse<Boolean>
    suspend fun uploadPost(uploadNewPostRequest: UploadNewPostRequest): ApiResponse<Boolean>
}