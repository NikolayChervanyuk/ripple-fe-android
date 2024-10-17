package com.mobi.ripple.core.data.common.converter

import androidx.room.TypeConverter
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType

class ChatEventConverter {

    @TypeConverter
    fun toChatEventType(value: String) = enumValueOf<ChatEventType>(value)

    @TypeConverter
    fun fromChatEventType(value: ChatEventType) = value.name
}