package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import kotlinx.serialization.Serializable

@Serializable
data class UserSimplePostsResponse(
    val simplePosts: List<SimplePosts>
) {
    @Serializable
    data class SimplePosts(
        val id: String,
        val image: ByteArray,
        val authorId: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SimplePosts

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

    fun asUserProfileSimplePosts(): List<UserProfileSimplePost> {
        val simplePostsList = ArrayList<UserProfileSimplePost>()

        for (post in simplePosts) {
            simplePostsList.add(
                UserProfileSimplePost(
                    post.id,
                    post.image,
                    post.authorId
                )
            )
        }
        return simplePostsList
    }
}
