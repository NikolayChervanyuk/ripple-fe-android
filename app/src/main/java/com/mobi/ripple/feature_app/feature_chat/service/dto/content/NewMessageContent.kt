package com.mobi.ripple.feature_app.feature_chat.service.dto.content

import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("new_message")
data class NewMessageContent @OptIn(ExperimentalSerializationApi::class) constructor(
//    @EncodeDefault val type: ChatEventType = ChatEventType.NEW_MESSAGE,
    val senderId: String,
    val chatId: String,
    val message: String,
    val fileName: String?,
    val fileExtension: String?
) : GenericContent//()
