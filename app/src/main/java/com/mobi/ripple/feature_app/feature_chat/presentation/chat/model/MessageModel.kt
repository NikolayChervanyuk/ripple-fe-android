package com.mobi.ripple.feature_app.feature_chat.presentation.chat.model

import androidx.compose.ui.graphics.ImageBitmap
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewMessageContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.NewMessage
import kotlinx.serialization.json.Json
import java.time.Instant

data class MessageModel(
    val messageId: Long,
    val eventType: ChatEventType,
    val chatId: String,
    val sentDate: Instant,
    val defaultTextMessage: String,
    val isMine: Boolean,
    val isUnread: Boolean,
    val isSent: Boolean,
    val messageDataJson: String,
    val authorUsername: String?,
    val authorPfp: ImageBitmap?,
    val isAuthorActive: Boolean
) {
    fun toNewMessage() = NewMessage(
        eventType = ChatEventType.NEW_MESSAGE,
        sentDate = Instant.now(),
        messageData = Json.decodeFromString<NewMessageContent>(messageDataJson)
    )
}
