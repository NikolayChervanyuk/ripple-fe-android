package com.mobi.ripple.core.domain.model.followers_following

data class FollowersFollowingSimpleUser(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val profilePicture: ByteArray?
)