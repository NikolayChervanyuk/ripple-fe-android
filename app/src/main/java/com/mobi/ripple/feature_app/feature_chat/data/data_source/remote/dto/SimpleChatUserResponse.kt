@file:UseSerializers(InstantSerializer::class)
package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.dto

import android.util.Base64
import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class SimpleChatUserResponse(
    val participantId: String,
    val name: String?,
    val username: String,
    val active: Boolean,
    val lastActive: Instant,
    val smallProfilePicture: String?
) {
    fun asSimpleChatUser() = SimpleChatUser(
        id = participantId,
        fullName = name,
        username = username,
        active = active,
        userPfp = smallProfilePicture?.let { Base64.decode(it, Base64.DEFAULT) }

    )
}
