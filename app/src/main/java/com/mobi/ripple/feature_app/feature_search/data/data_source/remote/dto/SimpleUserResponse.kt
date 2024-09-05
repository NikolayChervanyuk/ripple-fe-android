package com.mobi.ripple.feature_app.feature_search.data.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.feature_app.feature_search.domain.model.SimpleUser
import kotlinx.serialization.Serializable

@Serializable
data class SimpleUserResponse(
    val id: String,
    val fullName: String?,
    val username: String,
    val active: Boolean,
    val smallProfilePicture: String?
) {
    fun asSimpleUser() = SimpleUser(
        id = id,
        fullName = fullName,
        username = username,
        isActive = active,
        profilePicture = smallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) }
    )
}