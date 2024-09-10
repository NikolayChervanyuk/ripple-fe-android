@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.core.data.data_source.remote.post.dto

import android.util.Base64
import com.mobi.ripple.core.domain.model.post.Comment
import com.mobi.ripple.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class PostCommentResponse(
    val commentId: String,
    val authorProfilePicture: String?,
    val authorName: String?,
    val authorUsername: String,
    val createdDate: Instant,
    val lastUpdatedDate: Instant,
    val likesCount: Long,
    val liked: Boolean,
    val repliesCount: Long,
    val comment: String
) {

    fun asComment() = Comment(
        commentId = commentId,
        authorProfilePicture = authorProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) },
        authorName = authorName,
        authorUsername = authorUsername,
        createdDate = createdDate,
        lastUpdatedDate = lastUpdatedDate,
        likesCount = likesCount,
        liked = liked,
        repliesCount = repliesCount,
        comment = comment
    )
}
