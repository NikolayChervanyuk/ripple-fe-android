package com.mobi.ripple.core.data.data_source.remote.profile.dto

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfilePicture
import kotlinx.serialization.Serializable

@Serializable
data class UserProfilePictureResponse(
    val image: ByteArray
) {

    fun asUserProfilePicture() = UserProfilePicture(image)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfilePictureResponse

        return image.contentEquals(other.image)
    }

    override fun hashCode(): Int {
        return image.contentHashCode()
    }
}