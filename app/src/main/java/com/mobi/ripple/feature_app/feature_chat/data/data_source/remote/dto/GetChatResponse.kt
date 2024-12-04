@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.dto

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ChatEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class GetChatResponse(
    val chatId: String,
    val chatName: String,
    val lastSentTime: Instant
) {
    fun asChatEntity() = ChatEntity(
        chatId = chatId,
        chatName = chatName,
        lastSentMessageTime = lastSentTime,
        chatPicture = null //TODO
    )
}
