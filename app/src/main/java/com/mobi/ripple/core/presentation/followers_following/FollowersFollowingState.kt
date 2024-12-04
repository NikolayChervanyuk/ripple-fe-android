package com.mobi.ripple.core.presentation.followers_following

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mobi.ripple.core.presentation.followers_following.model.FollowersFollowingSimpleUserModel

data class FollowersFollowingState(
    var isMeState: MutableState<Boolean> = mutableStateOf(false),
    var followersList: SnapshotStateList<FollowersFollowingSimpleUserModel> = mutableStateListOf(),
    var followingList: SnapshotStateList<FollowersFollowingSimpleUserModel> = mutableStateListOf(),
    var followersPage: Int = 0,
    var followingPage: Int = 0
)