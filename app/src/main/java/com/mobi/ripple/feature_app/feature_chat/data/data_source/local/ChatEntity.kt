package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val chatName: String,
    val lastSentMessageTime: Instant,
    val chatPicture: ByteArray?
)