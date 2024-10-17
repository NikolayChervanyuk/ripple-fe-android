package com.mobi.ripple.feature_app.feature_chat.service.dto.content

import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("participant_removed")
data class ParticipantRemovedContent @OptIn(ExperimentalSerializationApi::class) constructor(
//    @EncodeDefault val type: ChatEventType = ChatEventType.PARTICIPANT_REMOVED,
    val chatId: String,
    val removerId: String,
    val removedParticipantId: String
) : GenericContent//()
