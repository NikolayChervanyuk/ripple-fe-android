@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.service.dto.message

import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatCreatedContent
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class ChatCreatedMessage(
    override val eventType: ChatEventType,
    override val sentDate: Instant,
    override val messageData: ChatCreatedContent
) : GenericMessage

fun SimpleChat.toChatCreatedMessage() = ChatCreatedMessage(
    eventType = ChatEventType.CHAT_CREATED,
    sentDate = lastSentTime,
    messageData = ChatCreatedContent(
        chatId = chatId,
        creatorId = GlobalAppManager.storedId ?: "",
        chatName = chatName
    )
)