package com.mobi.ripple.core.data.followers_following.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.core.domain.followers_following.model.FollowersFollowingSimpleUser
import kotlinx.serialization.Serializable

@Serializable
data class FollowersFollowingSimpleUserResponse(
    val id: String,
    val fullName: String?,
    val username: String,
    val active: Boolean,
    val smallProfilePicture: String?
) {
    fun asFollowersFollowingSimpleUser() = FollowersFollowingSimpleUser(
        id = id,
        fullName = fullName,
        username = username,
        isActive = active,
        profilePicture = smallProfilePicture?.let {
            Base64.decode(smallProfilePicture, Base64.DEFAULT)
        }
    )
}
