package com.mobi.ripple.core.data.reply.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobi.ripple.core.domain.post.model.Reply
import java.time.Instant

@Entity
data class ReplyEntity(
    @PrimaryKey
    val replyId: String,
    val authorProfilePicture: ByteArray?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    val liked: Boolean,
    val reply: String
) {
    fun asReply() = Reply(
        replyId = replyId,
        authorProfilePicture = authorProfilePicture,
        authorName = authorName,
        authorUsername = authorUsername,
        createdDate = createdDate,
        lastUpdatedDate = lastUpdatedDate,
        likesCount = likesCount,
        liked = liked,
        reply = reply
    )
}