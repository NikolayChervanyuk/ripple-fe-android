@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto

import android.util.Base64
import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class NewChatResponse(
    val chatId: String,
    val chatName: String,
    val createDate: Instant,
    val chatPicture: String?
) {
    fun toSimpleChat() = SimpleChat(
        chatId = chatId,
        chatName = chatName,
        lastSentTime = createDate,
        lastChatMessage = "You created new group - $chatName",
        chatPicture = chatPicture?.let { Base64.decode(it, Base64.DEFAULT) },
        isUnread = false,
    )
}
