@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.core.data.data_source.remote.post.dto

import android.util.Base64
import com.mobi.ripple.core.domain.model.post.Post
import com.mobi.ripple.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class PostResponse(
    val id: String,
    val creationDate: Instant,
    val lastModifiedDate: Instant,
    val authorId: String,
    val authorFullName: String?,
    val authorUsername: String,
    val authorActive: Boolean,
    val authorSmallProfilePicture: String?,
    val postImage: String,
    val caption: String?,
    val likesCount: Long,
    val liked: Boolean,
    val commentsCount: Long
) {
    fun asPost() = Post(
        id = id,
        creationDate = creationDate,
        lastModifiedDate = lastModifiedDate,
        authorId = authorId,
        authorFullName = authorFullName,
        authorUsername = authorUsername,
        isAuthorActive = authorActive,
        authorPfp = authorSmallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) },
        postImage = Base64.decode(postImage, Base64.DEFAULT),
        caption = caption,
        likesCount = likesCount,
        liked = liked,
        commentsCount = commentsCount,
    )
}