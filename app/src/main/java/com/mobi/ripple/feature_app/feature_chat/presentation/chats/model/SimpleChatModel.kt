package com.mobi.ripple.feature_app.feature_chat.presentation.chats.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import java.time.Instant

data class SimpleChatModel(
    val chatId: String,
    val chatName: String,
    val chatPicture: ImageBitmap?,
    val lastChatMessage: String,
    val isUnread: Boolean,
    val lastSentTime: Instant
)

fun SimpleChat.asSimpleChatModel() = SimpleChatModel(
    chatId = chatId,
    chatName = chatName,
    chatPicture = chatPicture?.let { BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap() },
    lastChatMessage = lastChatMessage,
    isUnread = isUnread,
    lastSentTime = lastSentTime
)
