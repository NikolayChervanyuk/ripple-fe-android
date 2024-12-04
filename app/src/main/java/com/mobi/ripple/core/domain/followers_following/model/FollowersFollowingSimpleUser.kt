package com.mobi.ripple.core.domain.followers_following.model

data class FollowersFollowingSimpleUser(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val profilePicture: ByteArray?
)