package com.mobi.ripple.feature_app.feature_profile.domain.model

import java.time.Instant

data class UserProfileInfo(
    val fullName: String?,
    val userName: String,
    val email: String?,
    val bio: String?,
    val followers: Long,
    val following: Long,
    val isFollowed: Boolean,
    val isActive: Boolean,
    val lastActive: Instant,
    val postsCount: Long,
)
