package com.mobi.ripple.core.domain.model.post

import java.time.Instant

data class Post(
    val id: String,
    val creationDate: Instant,
    val lastModifiedDate: Instant,
    val authorId: String,
    val postImage: ByteArray,
    val caption: String?,
    val likesCount: Long,
    val liked: Boolean,
    val commentsCount: Long
)