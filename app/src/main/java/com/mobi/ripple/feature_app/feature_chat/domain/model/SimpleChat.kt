package com.mobi.ripple.feature_app.feature_chat.domain.model

import java.time.Instant

data class SimpleChat(
    val chatId: String,
    val chatName: String,
    val chatPicture: ByteArray?,
    val lastChatMessage: String,
    val isUnread: Boolean,
    val lastSentTime: Instant
)
