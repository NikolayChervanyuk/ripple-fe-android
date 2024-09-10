package com.mobi.ripple.core.data.data_source.remote.followers_following.dto

import android.util.Base64
import com.mobi.ripple.core.domain.model.followers_following.FollowersFollowingSimpleUser
import kotlinx.serialization.Serializable

@Serializable
data class FollowersFollowingSimpleUserResponse(
    val id: String,
    val fullName: String?,
    val username: String,
    val active: Boolean,
    val smallProfilePicture: String
) {

    fun asFollowersFollowingSimpleUser() = FollowersFollowingSimpleUser(
        id = id,
        fullName = fullName,
        username = username,
        isActive = active,
        profilePicture = Base64.decode(smallProfilePicture, Base64.DEFAULT)
    )
}
