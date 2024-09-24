package com.mobi.ripple.core.domain.post.model

data class PostSimpleUser(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val profilePicture: ByteArray?
)
