package com.mobi.ripple.core.domain.profile.model

data class UserProfilePicture(
    val image: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
            //TODO: Prof pictre
        other as UserProfilePicture

        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}