package com.mobi.ripple.feature_app.feature_chat.service.dto.content

import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("chat_opened")
data class ChatOpenedContent @OptIn(ExperimentalSerializationApi::class) constructor(
//    @EncodeDefault val type: ChatEventType = ChatEventType.CHAT_OPENED,
    val userId: String,
    val chatId: String
) : GenericContent//()
