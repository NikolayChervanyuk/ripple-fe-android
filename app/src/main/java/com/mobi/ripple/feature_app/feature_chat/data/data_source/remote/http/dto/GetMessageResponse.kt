@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatCreatedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatOpenedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.GenericContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewMessageContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewParticipantContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantLeftContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantRemovedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatCreatedMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatOpenedMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.GenericMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.NewMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.NewParticipantMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ParticipantLeftMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ParticipantRemovedMessage
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

@Serializable(with = GetMessageResponseSerializer::class)
data class GetMessageResponse(
    val eventType: ChatEventType,
    val sentDate: Instant,
    @Polymorphic val messageContent: GenericContent
) {
    fun asGenericMessage(): GenericMessage {
        return when (eventType) {
            ChatEventType.NEW_MESSAGE -> NewMessage(
                eventType,
                sentDate,
                messageContent as NewMessageContent
            )

            ChatEventType.CHAT_OPENED -> ChatOpenedMessage(
                eventType,
                sentDate,
                messageContent as ChatOpenedContent
            )

            ChatEventType.CHAT_CREATED -> ChatCreatedMessage(
                eventType,
                sentDate,
                messageContent as ChatCreatedContent
            )

            ChatEventType.NEW_PARTICIPANT -> NewParticipantMessage(
                eventType,
                sentDate,
                messageContent as NewParticipantContent
            )

            ChatEventType.PARTICIPANT_LEFT -> ParticipantLeftMessage(
                eventType,
                sentDate,
                messageContent as ParticipantLeftContent
            )

            ChatEventType.PARTICIPANT_REMOVED -> ParticipantRemovedMessage(
                eventType,
                sentDate,
                messageContent as ParticipantRemovedContent
            )
        }
    }
}

object GetMessageResponseSerializer : KSerializer<GetMessageResponse> {

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GetMessageResponse") {
        element("eventType", ChatEventType.serializer().descriptor)
        element("sentDate", InstantSerializer.descriptor)
        element("messageContent", buildSerialDescriptor("GenericContent", PolymorphicKind.SEALED))
    }

    override fun serialize(encoder: Encoder, value: GetMessageResponse) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeSerializableElement(descriptor, 0, ChatEventType.serializer(), value.eventType)
        composite.encodeSerializableElement(descriptor, 1, InstantSerializer, value.sentDate)
        composite.encodeSerializableElement(descriptor, 2, serializerForType(value.eventType), value.messageContent)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): GetMessageResponse {
        val dec = decoder.beginStructure(descriptor)
        var eventType: ChatEventType? = null
        var sentDate: Instant? = null
        var content: GenericContent? = null

         while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> eventType = dec.decodeSerializableElement(descriptor, 0, ChatEventType.serializer())
                1 -> sentDate = dec.decodeSerializableElement(descriptor, 1, InstantSerializer)
                2 -> {
                    eventType?.let {
                        content = dec.decodeSerializableElement(descriptor, 2, serializerForType(it))
                    }
                }
                CompositeDecoder.DECODE_DONE -> break
                else -> throw SerializationException("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)

        return GetMessageResponse(eventType!!, sentDate!!, content!!)
    }

    @Suppress("UNCHECKED_CAST")
    private fun serializerForType(eventType: ChatEventType): KSerializer<GenericContent> {
        return when (eventType) {
            ChatEventType.NEW_MESSAGE -> NewMessageContent.serializer() as KSerializer<GenericContent>
            ChatEventType.CHAT_CREATED -> ChatCreatedContent.serializer() as KSerializer<GenericContent>
            ChatEventType.CHAT_OPENED -> ChatOpenedContent.serializer() as KSerializer<GenericContent>
            ChatEventType.NEW_PARTICIPANT -> NewParticipantContent.serializer() as KSerializer<GenericContent>
            ChatEventType.PARTICIPANT_LEFT -> ParticipantLeftContent.serializer() as KSerializer<GenericContent>
            ChatEventType.PARTICIPANT_REMOVED -> ParticipantRemovedContent.serializer() as KSerializer<GenericContent>
        }
    }
}