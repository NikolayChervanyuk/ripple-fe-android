package com.mobi.ripple.feature_app.feature_chat.service.util

import androidx.compose.ui.util.fastMap
import androidx.room.withTransaction
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ChatEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.MessageEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ParticipantChatEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ParticipantEntity
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser
import com.mobi.ripple.feature_app.feature_chat.domain.use_case.ChatUseCases
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.ChatEventType
import com.mobi.ripple.feature_app.feature_chat.service.dto.message.GenericMessage
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatCreatedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ChatOpenedContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewMessageContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.NewParticipantContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantLeftContent
import com.mobi.ripple.feature_app.feature_chat.service.dto.content.ParticipantRemovedContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

class MessageCacheManager(
    val database: AppDatabase,
    val chatUseCases: ChatUseCases
) {
    suspend fun cache(
        message: GenericMessage,
        isMine: Boolean = false,
        isUnread: Boolean = !isMine
    ): MessageEntity? {
        //TODO: create listener in GlobalAppManager
        val messageEntity: MessageEntity
        when (message.eventType) {
            ChatEventType.NEW_MESSAGE -> {
                val content = message.messageData as NewMessageContent
                messageEntity = MessageEntity(
                    id = 0,
                    eventType = message.eventType,
                    chatId = content.chatId,
                    sentDate = message.sentDate,
                    defaultTextMessage = content.message,
                    isMine = isMine,
                    isUnread = isUnread,
                    messageDataJson = Json.encodeToString(content)
                )
                database.messageDao.upsertMessage(messageEntity)
            }

            ChatEventType.CHAT_OPENED -> {
                val content = message.messageData as ChatOpenedContent
                val openedChatUser = database.participantDao.getById(content.userId)
                messageEntity = MessageEntity(
                    id = 0,
                    eventType = message.eventType,
                    chatId = content.chatId,
                    sentDate = message.sentDate,
                    defaultTextMessage = "${openedChatUser.username} opened chat",
                    isMine = isMine,
                    isUnread = isUnread,
                    messageDataJson = Json.encodeToString(content)
                )
                database.messageDao.upsertMessage(messageEntity)
            }

            ChatEventType.CHAT_CREATED -> {
                val participants =
                    chatUseCases.getChatParticipantsUseCase(
                        MessageResolver.getChatId(
                            message
                        )
                    )
                return if (participants != null) {
                    cacheNewChatMessage(
                        message,
                        participants,
                        isMine,
                        isUnread
                    )
                } else {
                    Timber.w("Chat participants can't be retrieved because API returned error response")
                    null
                }
            }

            ChatEventType.NEW_PARTICIPANT -> {
                val content = message.messageData as NewParticipantContent
                val inviter = database.participantDao.getById(content.inviterId)
                val invitedUserResponse = chatUseCases.getChatUserUseCase(content.participantId)
                val defaultMessage =
                    if (!invitedUserResponse.isError) {
                        val invitedUser = invitedUserResponse.content!!
                        database.withTransaction {
                            database.participantChatDao.upsert(
                                ParticipantChatEntity(
                                    id = 0,
                                    participantId = invitedUser.id,
                                    chatId = content.chatId
                                )
                            )
                            database.participantDao.upsert(
                                ParticipantEntity(
                                    id = invitedUser.id,
                                    fullName = invitedUser.fullName,
                                    username = invitedUser.username,
                                    profilePicture = invitedUser.userPfp
                                )
                            )
                        }
                        "${inviter.username} added ${invitedUser.username}"
                    } else "${inviter.username} added new member"
                messageEntity = MessageEntity(
                    id = 0,
                    eventType = message.eventType,
                    chatId = content.chatId,
                    sentDate = message.sentDate,
                    defaultTextMessage = defaultMessage, //getDefaultChatMessageText(message),
                    isMine = isMine,
                    isUnread = isUnread,
                    messageDataJson = Json.encodeToString(content)
                )
                database.messageDao.upsertMessage(messageEntity)
            }

            ChatEventType.PARTICIPANT_LEFT -> {
                val content = message.messageData as ParticipantLeftContent
                var leftUsername = "You"
                content.participantId?.let {
                    leftUsername = database.participantDao.getById(content.participantId).username
                }
                messageEntity = MessageEntity(
                    id = 0,
                    eventType = message.eventType,
                    chatId = content.chatId,
                    sentDate = message.sentDate,
                    defaultTextMessage = "$leftUsername left the chat",
                    isMine = isMine,
                    isUnread = isUnread,
                    messageDataJson = Json.encodeToString(content)
                )
                database.messageDao.upsertMessage(messageEntity)
                //TODO("remove myself from group")
            }

            ChatEventType.PARTICIPANT_REMOVED -> {
                val content = message.messageData as ParticipantRemovedContent
                val removerUsername =
                    database.participantDao.getById(content.removerId).username
                val removedUsername =
                    database.participantDao.getById(content.removedUserId).username
                messageEntity = MessageEntity(
                    id = 0,
                    eventType = message.eventType,
                    chatId = content.chatId,
                    sentDate = message.sentDate,
                    defaultTextMessage = "$removerUsername removed $removedUsername from the group",
                    isMine = isMine,
                    isUnread = isUnread,
                    messageDataJson = Json.encodeToString(content)
                )
                database.messageDao.upsertMessage(messageEntity)
            }
        }
        database.chatDao.updateChat(
            database.chatDao.getChatById(MessageResolver.getChatId(message))
                .copy(lastSentMessageTime = message.sentDate)
        )
        return messageEntity
    }

    private suspend fun cacheNewChatMessage(
        message: GenericMessage,
        participants: List<SimpleChatUser>,
        isMine: Boolean,
        isUnread: Boolean
    ): MessageEntity {
        val content = message.messageData as ChatCreatedContent
        database.withTransaction {

            database.chatDao.upsertChat(
                ChatEntity(
                    chatId = content.chatId,
                    chatName = content.chatName,
                    lastSentMessageTime = message.sentDate,
                    chatPicture = null
                )
            )
            database.participantChatDao.upsertAll(
                participants.fastMap {
                    ParticipantChatEntity(
                        id = 0,
                        participantId = it.id,
                        chatId = content.chatId
                    )
                }
            )
            database.participantDao.upsertAll(participants.fastMap {
                ParticipantEntity(
                    id = it.id,
                    username = it.username,
                    fullName = it.fullName,
                    profilePicture = it.userPfp
                )
            })
        }
        val creator = participants.find { it.id == content.creatorId }
        val messageEntity = MessageEntity(
            id = 0,
            eventType = message.eventType,
            chatId = content.chatId,
            sentDate = message.sentDate,
            defaultTextMessage = creator?.let {
                "${creator.username} created group ${content.chatName}"
            } ?: "New group created - ${content.chatName}",
            isMine = isMine,
            isUnread = isUnread,
            messageDataJson = Json.encodeToString(content)
        )
        database.messageDao.upsertMessage(messageEntity)
        return messageEntity
    }
}