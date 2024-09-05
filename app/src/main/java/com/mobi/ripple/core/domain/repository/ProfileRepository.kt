package com.mobi.ripple.core.domain.repository

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import com.mobi.ripple.feature_app.feature_profile.domain.repository.PersonalProfileRepository

interface ProfileRepository {
    suspend fun getUserProfilePicture(username: String): Response<UserProfilePicture?>
    suspend fun getUserProfileInfo(username: String): Response<UserProfileInfo?>
    suspend fun getSimpleUserPosts(
        username: String,
        page: Long,
    ): Response<List<UserProfileSimplePost>>

}