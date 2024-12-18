package com.mobi.ripple.core.domain.post.model

import java.time.Instant

data class Post(
    val id: String,
    val creationDate: Instant,
    val lastModifiedDate: Instant,
    val authorId: String,
    val authorFullName: String?,
    val authorUsername: String,
    val isAuthorActive: Boolean,
    val authorPfp: ByteArray?,
    val postImage: ByteArray,
    val caption: String?,
    val likesCount: Long,
    val liked: Boolean,
    val commentsCount: Long
)