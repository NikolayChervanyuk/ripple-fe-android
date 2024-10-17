package com.mobi.ripple.feature_app.feature_chat.service.util

import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.GenericMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatCreatedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatOpenedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewMessageContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewParticipantContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantLeftContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantRemovedContent

class MessageResolver {

    companion object {

        fun getChatId(message: GenericMessage): String {
            return when (message.eventType) {
                ChatEventType.NEW_MESSAGE -> (message.messageData as NewMessageContent).chatId
                ChatEventType.CHAT_OPENED -> (message.messageData as ChatOpenedContent).chatId
                ChatEventType.CHAT_CREATED -> (message.messageData as ChatCreatedContent).chatId
                ChatEventType.NEW_PARTICIPANT -> (message.messageData as NewParticipantContent).chatId
                ChatEventType.PARTICIPANT_LEFT -> (message.messageData as ParticipantLeftContent).chatId
                ChatEventType.PARTICIPANT_REMOVED -> (message.messageData as ParticipantRemovedContent).chatId
            }
        }

        fun getSenderId(message: GenericMessage): String {
            return when (message.eventType) {
                ChatEventType.NEW_MESSAGE -> (message.messageData as NewMessageContent).senderId
                ChatEventType.CHAT_OPENED -> (message.messageData as ChatOpenedContent).userId
                ChatEventType.CHAT_CREATED -> (message.messageData as ChatCreatedContent).creatorId
                ChatEventType.NEW_PARTICIPANT -> (message.messageData as NewParticipantContent).inviterId
                ChatEventType.PARTICIPANT_LEFT -> (message.messageData as ParticipantLeftContent).participantId
                    ?: GlobalAppManager.storedId ?: ""

                ChatEventType.PARTICIPANT_REMOVED -> (message.messageData as ParticipantRemovedContent).removerId
            }
        }
    }
}