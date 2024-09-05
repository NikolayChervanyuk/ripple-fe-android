package com.mobi.ripple.core.presentation.profile.model

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture

data class UserProfilePictureModel(
    val image: ByteArray
) {

    fun UserProfilePicture.asUserProfilePictureModel() =
        UserProfilePictureModel(image)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfilePictureModel

        return image.contentEquals(other.image)
    }
    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}
