package com.mobi.ripple.core.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mobi.ripple.core.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.core.presentation.profile.model.UserProfileSimplePostModel

data class ProfileState(
    val userProfileInfoState: MutableState<UserProfileInfoModel> = mutableStateOf(
        UserProfileInfoModel(
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
    val userProfileSimplePosts: SnapshotStateList<UserProfileSimplePostModel> = mutableStateListOf(),
    var page: Long = 0L
)