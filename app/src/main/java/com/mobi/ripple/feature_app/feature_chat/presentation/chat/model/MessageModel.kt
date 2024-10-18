package com.mobi.ripple.feature_app.feature_chat.presentation.chat.model

import androidx.compose.ui.graphics.ImageBitmap
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import java.time.Instant

data class MessageModel(
    val messageId: Long,
    val eventType: ChatEventType,
    val chatId: String,
    val sentDate: Instant,
    val defaultTextMessage: String,
    val isMine: Boolean,
    val isUnread: Boolean,
    val messageDataJson: String,
    val authorUsername: String?,
    val authorPfp: ImageBitmap?,
    val isAuthorActive: Boolean
)
