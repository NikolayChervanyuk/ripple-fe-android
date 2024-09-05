package com.mobi.ripple.feature_app.feature_profile.data.repository

import android.util.Base64
import com.mobi.ripple.core.data.data_source.remote.profile.ProfileApiService
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost

open class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService
): ProfileRepository {
    override suspend fun getUserProfilePicture(username: String): Response<UserProfilePicture?> {
        val apiResponse = profileApiService.getUserProfilePicture(username)

        return apiResponse.toResponse(
            UserProfilePicture(apiResponse.content?.let { image ->
                Base64.decode(image, Base64.DEFAULT)
            } ?: ByteArray(0))
        )
    }

    override suspend fun getUserProfileInfo(username: String): Response<UserProfileInfo?> {
        val apiResponse = profileApiService.getUserProfileInfo(username)

        return apiResponse.toResponse(apiResponse.content?.asUserProfileInfo())
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