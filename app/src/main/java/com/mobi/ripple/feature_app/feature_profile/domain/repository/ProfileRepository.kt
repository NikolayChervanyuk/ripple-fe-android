package com.mobi.ripple.feature_app.feature_profile.domain.repository

import com.mobi.ripple.core.domain.Response
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost

interface ProfileRepository {
    suspend fun getUserProfilePicture(username: String): Response<UserProfilePicture?>
    suspend fun uploadUserProfilePicture(image: ByteArray): Response<Boolean?>
    suspend fun deleteUserProfilePicture(): Response<Boolean?>

    suspend fun getUserProfileInfo(username: String): Response<UserProfileInfo?>

    suspend fun isOtherUserWithUsernameExists(username: String): Response<Boolean?>
    suspend fun isOtherUserWithEmailExists(email: String): Response<Boolean?>
    suspend fun editProfileInfo(userProfileInfo: UserProfileInfo): Response<Boolean?>

    //    suspend fun uploadPost(username: String): Response<?>
    suspend fun getSimpleUserPosts(
        username: String,
        page: Long,
    ): Response<List<UserProfileSimplePost>>
}