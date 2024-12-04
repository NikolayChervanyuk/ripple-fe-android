package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobi.ripple.feature_app.feature_chat.domain.model.Message
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import java.time.Instant

@Entity
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val eventType: ChatEventType,
    val chatId: String,
    val sentDate: Instant,
    val defaultTextMessage: String,
    val isMine: Boolean,
    val isUnread: Boolean,
    val isSent: Boolean,
    val messageDataJson: String
) {

    fun toMessage() = Message(
        messageId = id,
        eventType = eventType,
        chatId = chatId,
        sentDate = sentDate,
        defaultTextMessage = defaultTextMessage,
        isMine = isMine,
        isUnread = isUnread,
        isSent = isSent,
        messageDataJson = messageDataJson
    )
}
