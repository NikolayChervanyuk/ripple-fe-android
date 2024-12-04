package com.mobi.ripple.feature_auth.data.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.feature_auth.domain.model.SimpleAuthUser
import kotlinx.serialization.Serializable

@Serializable
data class SimpleAuthUserResponse(
   val id: String,
   val fullName: String?,
   val username: String,
   val smallProfilePicture: String?,
   val active: Boolean,
) {
    fun toSimpleAuthUser() = SimpleAuthUser(
        userId = id,
        smallPfp = smallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) }
    )
}
