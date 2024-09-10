package com.mobi.ripple.feature_app.feature_profile.data.repository

import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.repository.ProfileRepositoryImpl
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.PersonalProfileApiService
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.asUpdateUserProfileInfoRequest
import com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto.asUploadPostRequest
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileNewPost
import com.mobi.ripple.feature_app.feature_profile.domain.repository.PersonalProfileRepository

class PersonalProfileRepositoryImpl(
    private val personalProfileApiService: PersonalProfileApiService,
    private val database: AppDatabase
) : PersonalProfileRepository, ProfileRepositoryImpl(personalProfileApiService, database) {


    override suspend fun uploadUserProfilePicture(
        image: ByteArray
    ): Response<Boolean?> {
        val apiResponse = personalProfileApiService.uploadUserProfilePicture(image)

        return apiResponse.toResponse(apiResponse.content)
    }


    override suspend fun deleteUserProfilePicture(): Response<Boolean?> {
        val apiResponse = personalProfileApiService.deleteUserProfilePicture()

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun isOtherUserWithUsernameExists(username: String): Response<Boolean?> {
        val apiResponse = personalProfileApiService.isOtherUserWithUsernameExists(username)

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun isOtherUserWithEmailExists(email: String): Response<Boolean?> {
        val apiResponse = personalProfileApiService.isOtherUserWithEmailExists(email)

        return apiResponse.toResponse(apiResponse.content)
    }


    override suspend fun editProfileInfo(userProfileInfo: UserProfileInfo): Response<Boolean?> {
        val apiResponse = personalProfileApiService.editProfileInfo(
            userProfileInfo.asUpdateUserProfileInfoRequest()
        )

        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun uploadPost(newPost: UserProfileNewPost): Response<Boolean?> {
        val apiResponse = personalProfileApiService.uploadPost(newPost.asUploadPostRequest())

        return apiResponse.toResponse(apiResponse.content)
    }
}