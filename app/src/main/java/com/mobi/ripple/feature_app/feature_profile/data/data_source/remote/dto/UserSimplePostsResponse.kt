package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import kotlinx.serialization.Serializable

@Serializable
data class UserSimplePostsResponse(
    val id: String,
    val image: ByteArray,
    val authorId: String
) {

    fun asUserProfileSimplePost(): UserProfileSimplePost {
        return UserProfileSimplePost(
            id = id,
            image = image,
            authorId = authorId
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserSimplePostsResponse

        if (id != other.id) return false
        if (!image.contentEquals(other.image)) return false
        if (authorId != other.authorId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + authorId.hashCode()
        return result
    }
}

