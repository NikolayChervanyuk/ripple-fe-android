package com.mobi.ripple.feature_app.feature_profile.data.repository

import android.util.Base64
import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.ProfileApiService
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.asUpdateUserProfileInfoRequest
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import com.mobi.ripple.feature_app.feature_profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService
) : ProfileRepository {
    override suspend fun getUserProfilePicture(username: String): Response<UserProfilePicture?> {
        val apiResponse = profileApiService.getUserProfilePicture(username)

        return apiResponse.toResponse(
            UserProfilePicture(apiResponse.content?.let { image ->
                Base64.decode(image, Base64.DEFAULT)
            } ?: ByteArray(0))
        )
    }

    override suspend fun uploadUserProfilePicture(
        image: ByteArray
    ): Response<Boolean?> {
        val apiResponse = profileApiService.uploadUserProfilePicture(image)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun deleteUserProfilePicture(): Response<Boolean?> {
        val apiResponse = profileApiService.deleteUserProfilePicture()

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun getUserProfileInfo(username: String): Response<UserProfileInfo?> {
        val apiResponse = profileApiService.getUserProfileInfo(username)

        return apiResponse.toResponse(apiResponse.content?.asUserProfileInfo())
    }

    override suspend fun isOtherUserWithUsernameExists(username: String): Response<Boolean?> {
        val apiResponse = profileApiService.isOtherUserWithUsernameExists(username)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun isOtherUserWithEmailExists(email: String): Response<Boolean?> {
        val apiResponse = profileApiService.isOtherUserWithEmailExists(email)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun editProfileInfo(userProfileInfo: UserProfileInfo): Response<Boolean?> {
        val apiResponse = profileApiService.editProfileInfo(
            userProfileInfo.asUpdateUserProfileInfoRequest()
        )

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun getSimpleUserPosts(
        username: String,
        page: Long,
    ): Response<List<UserProfileSimplePost>> {
        val apiResponse = profileApiService.getSimplePosts(username, page)

        return apiResponse.toResponse(
            apiResponse.content?.map { it.asUserProfileSimplePost() }
        )
    }
}