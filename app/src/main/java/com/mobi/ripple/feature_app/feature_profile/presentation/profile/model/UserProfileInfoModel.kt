package com.mobi.ripple.feature_app.feature_profile.presentation.profile.model

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileInfo
import java.time.Instant

data class UserProfileInfoModel(
    val fullName: String?,
    val userName: String,
    val bio: String?,
    val followers: Long,
    val following: Long,
    val isFollowed: Boolean,
    val isActive: Boolean,
    val lastActive: Instant?,
    val postsCount: Long
)

fun UserProfileInfo.asUserProfileInfoModel() = UserProfileInfoModel(
    fullName = fullName,
    userName = userName,
    bio = bio,
    followers = followers,
    following = following,
    isFollowed = isFollowed,
    isActive = isActive,
    lastActive = lastActive,
    postsCount = postsCount
)
