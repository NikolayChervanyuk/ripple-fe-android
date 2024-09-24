package com.mobi.ripple.core.domain.post.model

import java.time.Instant

data class Comment(
    val commentId: String,
    val authorProfilePicture: ByteArray?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    val liked: Boolean,
    val repliesCount: Long,
    val comment: String
)
