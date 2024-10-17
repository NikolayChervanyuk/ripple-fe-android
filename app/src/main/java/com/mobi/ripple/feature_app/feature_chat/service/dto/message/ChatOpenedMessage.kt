@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.service.dto.message

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatOpenedContent
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class ChatOpenedMessage(
    override val eventType: ChatEventType,
    override val sentDate: Instant,
    override val messageData: ChatOpenedContent
) : GenericMessage
