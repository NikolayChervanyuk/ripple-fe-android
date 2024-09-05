package com.mobi.ripple.feature_app.feature_profile.domain.repository

import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileNewPost

interface PersonalProfileRepository: ProfileRepository {
    suspend fun uploadUserProfilePicture(image: ByteArray): Response<Boolean?>
    suspend fun deleteUserProfilePicture(): Response<Boolean?>
    suspend fun isOtherUserWithUsernameExists(username: String): Response<Boolean?>
    suspend fun isOtherUserWithEmailExists(email: String): Response<Boolean?>
    suspend fun editProfileInfo(userProfileInfo: UserProfileInfo): Response<Boolean?>
    suspend fun uploadPost(newPost: UserProfileNewPost): Response<Boolean?>
}