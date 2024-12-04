package com.mobi.ripple.feature_app.feature_chat.domain.model

import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import java.time.Instant

data class Message(
    val messageId: Long,
    val eventType: ChatEventType,
    val chatId: String,
    val sentDate: Instant,
    val defaultTextMessage: String,
    val isMine: Boolean,
    val isUnread: Boolean,
    val isSent: Boolean,
    val messageDataJson: String
)
