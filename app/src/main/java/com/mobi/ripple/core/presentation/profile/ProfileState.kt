package com.mobi.ripple.core.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.UserProfileSimplePostModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ProfileState(
    val userProfileInfoState: MutableState<UserProfileInfoModel> = mutableStateOf(
        UserProfileInfoModel(
            id = "",
            fullName = "",
            userName = "",
            email = "",
            bio = "",
            followers = 0L,
            following = 0L,
            isFollowed = false,
            isActive = false,
            lastActive = null,
            postsCount = 0L
        )
    ),
    val userProfilePicture: MutableState<ByteArray?> = mutableStateOf(null),
    //FIXME: use UserProfileSimplePostModel and decode at conversion stage
    var userProfileSimplePostsFlow: Flow<PagingData<UserProfileSimplePostModel>> = emptyFlow(),
)