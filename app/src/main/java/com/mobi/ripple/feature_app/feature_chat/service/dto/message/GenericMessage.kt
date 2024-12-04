@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.service.dto.message

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.GenericContent
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.Instant

@Serializable(with = GenericMessageSerializer::class)
sealed interface GenericMessage {
    val eventType: ChatEventType
    val sentDate: Instant
    val messageData: GenericContent
}

@Serializable
enum class ChatEventType(val literal: String) {
    @SerialName("new_message")
    NEW_MESSAGE("new_message"),

    @SerialName("chat_opened")
    CHAT_OPENED("chat_opened"),

    @SerialName("chat_created")
    CHAT_CREATED("chat_created"),

    @SerialName("new_participant")
    NEW_PARTICIPANT("new_participant"),

    @SerialName("participant_left")
    PARTICIPANT_LEFT("participant_left"),

    @SerialName("participant_removed")
    PARTICIPANT_REMOVED("participant_removed")
}

object GenericMessageSerializer :
    JsonContentPolymorphicSerializer<GenericMessage>(GenericMessage::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<GenericMessage> {
        return when (element.jsonObject["eventType"]?.jsonPrimitive?.content) {
            ChatEventType.NEW_MESSAGE.literal -> NewMessage.serializer()
            ChatEventType.CHAT_OPENED.literal -> ChatOpenedMessage.serializer()
            ChatEventType.CHAT_CREATED.literal -> ChatCreatedMessage.serializer()
            ChatEventType.NEW_PARTICIPANT.literal -> NewParticipantMessage.serializer()
            ChatEventType.PARTICIPANT_LEFT.literal -> ParticipantLeftMessage.serializer()
            ChatEventType.PARTICIPANT_REMOVED.literal -> ParticipantRemovedMessage.serializer()
            else -> throw Exception("Can't deserialize chat message")
        }
    }
}