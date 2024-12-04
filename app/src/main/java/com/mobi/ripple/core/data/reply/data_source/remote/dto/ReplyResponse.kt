@file:UseSerializers(InstantSerializer::class)
package com.mobi.ripple.core.data.reply.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.core.data.reply.data_source.local.ReplyEntity
import com.mobi.ripple.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class ReplyResponse(
    val id: String,
    val authorProfilePicture: String?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    val liked: Boolean,
    val reply: String
) {
    fun asReplyEntity() = ReplyEntity(
        replyId = id,
        authorProfilePicture = authorProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) },
        authorName = authorName,
        authorUsername = authorUsername,
        createdDate = createdDate,
        lastUpdatedDate = lastUpdatedDate,
        likesCount = likesCount,
        liked = liked,
        reply = reply
    )
}
