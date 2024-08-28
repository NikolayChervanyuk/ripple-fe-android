package com.mobi.ripple.feature_app.feature_profile.domain.model

data class UserProfileSimplePost(
    val id: String,
    val image : ByteArray,
    val authorId: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfileSimplePost

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
