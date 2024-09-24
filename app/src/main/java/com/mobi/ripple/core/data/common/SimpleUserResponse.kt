package com.mobi.ripple.core.data.common

import android.util.Base64
import com.mobi.ripple.feature_app.feature_search.domain.model.SearchSimpleUser
import kotlinx.serialization.Serializable

@Serializable
data class SimpleUserResponse(
    val id: String,
    val fullName: String?,
    val username: String,
    val active: Boolean,
    val smallProfilePicture: String?
) {
    fun asSimpleUser() = SearchSimpleUser(
        id = id,
        fullName = fullName,
        username = username,
        isActive = active,
        profilePicture = smallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) }
    )
}