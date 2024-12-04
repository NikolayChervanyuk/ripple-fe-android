@file:UseSerializers(InstantSerializer::class)

package com.mobi.ripple.feature_app.feature_chat.service.dto.message

import com.mobi.ripple.core.util.InstantSerializer
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewMessageContent
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Instant

@Serializable
data class NewMessage(
    override val eventType: ChatEventType,
    override val sentDate: Instant,
    override val messageData: NewMessageContent
) : GenericMessage