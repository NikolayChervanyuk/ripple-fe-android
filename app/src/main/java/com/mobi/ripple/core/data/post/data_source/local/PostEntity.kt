package com.mobi.ripple.core.data.post.data_source.local

import android.util.Base64
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
    val authorSmallProfilePicture: String?,
    val postImage: String,
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
        authorPfp = authorSmallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) },
        postImage = Base64.decode(postImage, Base64.DEFAULT),
        caption = caption,
        likesCount = likesCount,
        liked = liked,
        commentsCount = commentsCount
    )
}
