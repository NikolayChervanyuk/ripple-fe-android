package com.mobi.ripple.core.data.post.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobi.ripple.core.domain.post.model.Post
import java.time.Instant

@Entity
data class PostEntity(
    @PrimaryKey
    val id: String,
    val creationDate: Instant,
    val lastModifiedDate: Instant,
    val authorId: String,
    val authorFullName: String?,
    val authorUsername: String,
    val authorActive: Boolean,
    val authorSmallProfilePicture: ByteArray?,
    val postImage: ByteArray,
    val caption: String?,
    val likesCount: Long,
    val liked: Boolean,
    val commentsCount: Long,
    var page: Int = -1
) {
    fun asPost() = Post(
        id = id,
        creationDate = creationDate,
        lastModifiedDate = lastModifiedDate,
        authorId = authorId,
        authorFullName = authorFullName,
        authorUsername = authorUsername,
        isAuthorActive = authorActive,
        authorPfp = authorSmallProfilePicture,
        postImage = postImage,
        caption = caption,
        likesCount = likesCount,
        liked = liked,
        commentsCount = commentsCount
    )
}
