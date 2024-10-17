package com.mobi.ripple.feature_app.feature_chat.presentation.chat.model

import androidx.compose.ui.graphics.ImageBitmap
import com.mobi.ripple.feature_app.feature_chat.presentation.chats.model.SimpleChatModel
import com.mobi.ripple.feature_app.feature_chat.presentation.model.SimpleChatUserModel

data class ChatModel(
    val chatId: String,
    val chatName: String,
    val chatPicture: ImageBitmap?,
    var chatParticipants: Map<String, SimpleChatUserModel>
)

fun SimpleChatModel.toChatModel() = ChatModel(
    chatId = chatId,
    chatName = chatName,
    chatPicture = chatPicture,
    chatParticipants = mapOf()
)