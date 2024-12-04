package com.mobi.ripple.core.domain.profile.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.profile.model.UserProfileInfo
import com.mobi.ripple.core.domain.profile.model.UserProfilePicture
import com.mobi.ripple.core.domain.profile.model.UserProfileSimplePost
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfilePicture(username: String): Response<UserProfilePicture?>
    suspend fun getUserProfileInfo(username: String): Response<UserProfileInfo?>
    suspend fun getSimpleUserPostsFlow(
        username: String
    ): Flow<PagingData<UserProfileSimplePost>>
    suspend fun changeFollowingState(username: String): Response<Boolean?>
}