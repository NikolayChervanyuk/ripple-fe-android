package com.mobi.ripple.feature_app.feature_profile.presentation.profile

import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.UserProfileInfoModel
import com.mobi.ripple.feature_app.feature_profile.presentation.profile.model.UserProfileSimplePostModel

data class ProfileState(
    var userProfileInfo: UserProfileInfoModel = UserProfileInfoModel(
        fullName = "",
        userName = "",
        bio = "",
        followers = 0L,
        following = 0L,
        isFollowed = false,
        isActive = false,
        lastActive = null,
        postsCount = 0L
    ),
    var userProfilePicture: ByteArray = ByteArray(0),
    val userProfileSimplePosts: MutableList<UserProfileSimplePostModel> = mutableListOf(),
    var page: Long = 0L,
)
