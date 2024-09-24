package com.mobi.ripple.core.domain.post.model

import java.time.Instant

data class Reply(
    val replyId: String,
    val authorProfilePicture: ByteArray?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    val liked: Boolean,
    val reply: String
)
