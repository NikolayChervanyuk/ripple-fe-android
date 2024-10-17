@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.service.dto.message

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantRemovedContent
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class ParticipantRemovedMessage(
    override val eventType: ChatEventType,
    override val sentDate: Instant,
    override val messageData: ParticipantRemovedContent
) : GenericMessage