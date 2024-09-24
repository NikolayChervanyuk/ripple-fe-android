package com.mobi.ripple.core.data.post.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.core.domain.post.model.PostSimpleUser
import kotlinx.serialization.Serializable

@Serializable
data class PostSimpleUserResponse(
    val id: String,
    val fullName: String?,
    val username: String,
    val active: Boolean,
    val smallProfilePicture: String?
) {
    fun asPostSimpleUser() = PostSimpleUser(
        id = id,
        fullName = fullName,
        username = username,
        isActive = active,
        profilePicture = smallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) }
    )
}