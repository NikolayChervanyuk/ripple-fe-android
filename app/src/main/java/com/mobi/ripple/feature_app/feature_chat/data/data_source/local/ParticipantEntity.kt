package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser

@Entity
data class ParticipantEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    val fullName: String?,
    val profilePicture: ByteArray?
){
    fun asSimpleChatUser() = SimpleChatUser(
        id = id,
        username = username,
        fullName = fullName,
        active = false,
        userPfp = profilePicture
    )
}