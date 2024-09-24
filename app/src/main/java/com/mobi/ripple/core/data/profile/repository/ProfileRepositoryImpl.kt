package com.mobi.ripple.core.data.profile.repository

import android.util.Base64
import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.profile.data_source.pager.SimplePostPager
import com.mobi.ripple.core.data.profile.data_source.remote.ProfileApiService
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.core.domain.profile.model.UserProfileInfo
import com.mobi.ripple.core.domain.profile.model.UserProfilePicture
import com.mobi.ripple.core.domain.profile.model.UserProfileSimplePost
import com.mobi.ripple.core.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

open class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService,
    private val database: AppDatabase
) : ProfileRepository {

    override suspend fun getUserProfilePicture(username: String): Response<UserProfilePicture?> {
        val apiResponse = profileApiService.getUserProfilePicture(username)
        return apiResponse.toResponse(apiResponse.content?.let { image ->
            UserProfilePicture(Base64.decode(image, Base64.DEFAULT))
        })
    }

    override suspend fun getUserProfileInfo(username: String): Response<UserProfileInfo?> {
        val apiResponse = profileApiService.getUserProfileInfo(username)

        return apiResponse.toResponse(apiResponse.content?.asUserProfileInfo())
    }

    override suspend fun getSimpleUserPostsFlow(
        username: String
    ): Flow<PagingData<UserProfileSimplePost>> {
        return SimplePostPager(
            profileApiService,
            database,
            username
        ).getPager().flow.map { pagingData ->
            pagingData.map { it.asUserProfileSimplePost() }
        }
    }
//    override suspend fun getSimpleUserPosts(
//        username: String,
//        page: Long,
//    ): Response<List<UserProfileSimplePost>> {
//        val apiResponse = profileApiService.getSimplePosts(username, page)
//
//        return apiResponse.toResponse(
//            apiResponse.content?.map { it.asUserProfileSimplePost() }
//        )
//    }

    override suspend fun changeFollowingState(username: String): Response<Boolean?> {
        val apiResponse = profileApiService.changeFollowingState(username)

        return apiResponse.toResponse(apiResponse.content)
    }
}