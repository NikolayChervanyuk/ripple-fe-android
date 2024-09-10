package com.mobi.ripple.core.data.repository

import android.util.Base64
import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.data_source.pager.profile.SimplePostPager
import com.mobi.ripple.core.data.data_source.remote.profile.ProfileApiService
import com.mobi.ripple.core.domain.model.Response
import com.mobi.ripple.core.domain.repository.ProfileRepository
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
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