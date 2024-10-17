package com.mobi.ripple.feature_app.feature_chat.data.repository

import androidx.compose.ui.util.fastMap
import androidx.paging.PagingData
import androidx.paging.map
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_chat.data.data_source.pager.ChatsPager
import com.mobi.ripple.feature_app.feature_chat.data.data_source.pager.MessagesPager
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.ChatApiService
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto.NewChatRequest
import com.mobi.ripple.feature_app.feature_chat.domain.model.Message
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser
import com.mobi.ripple.feature_app.feature_chat.domain.repository.ChatRepository
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val apiService: ChatApiService,
    private val database: AppDatabase,
//    private val cacheManager: MessageCacheManager
) : ChatRepository {
    override suspend fun findUsersLike(queryText: String): Response<List<SimpleChatUser>?> {
        val apiResponse = apiService.findUsersLike(queryText)

        return apiResponse.toResponse(
            apiResponse.content?.map { user -> user.asSimpleChatUser() }
        )
    }

    override suspend fun createNewChat(
        chatName: String?,
        participantIds: List<String>
    ): Response<SimpleChat?> {
        val apiResponse = apiService.createNewChat(NewChatRequest(chatName, participantIds))

        return apiResponse.toResponse(apiResponse.content?.toSimpleChat())
    }

    override suspend fun getChatUser(userId: String): Response<SimpleChatUser?> {
        val apiResponse = apiService.getChatUser(userId)

        return apiResponse.toResponse(apiResponse.content?.asSimpleChatUser())
    }

    override suspend fun hasPendingMessages(): Response<Boolean?> {
        val apiResponse = apiService.hasPendingMessages()
        return apiResponse.toResponse(apiResponse.content)
    }

    override suspend fun getChatParticipants(chatId: String): List<SimpleChatUser>? {
        val localChatParticipants = database.participantDao.getAllByChatId(chatId)
        if (localChatParticipants.isNotEmpty()) return localChatParticipants.fastMap { it.asSimpleChatUser() }

        val apiResponse = apiService.getChatParticipants(chatId)
        if (apiResponse.isError) return null
        return apiResponse.content?.map { it.asSimpleChatUser() }
    }

    override suspend fun getChat(chatId: String): SimpleChat {
        val entity = database.chatDao.getChatById(chatId)
        val message = database.messageDao.getLatestMessageByChatId(chatId)
        return SimpleChat(
            chatId = entity.chatId,
            chatName = entity.chatName,
            chatPicture = entity.chatPicture,
            lastSentTime = entity.lastSentMessageTime,
            isUnread = message.isUnread,
            lastChatMessage = message.defaultTextMessage
        )
    }

    override suspend fun getChatsFlow(cacheManager: MessageCacheManager): Flow<PagingData<SimpleChat>> {
        return ChatsPager(database, cacheManager, apiService).getPager().flow
            .map { pagingData ->
                pagingData.map { entity ->
                    val message = database.messageDao.getLatestMessageByChatId(entity.chatId)
                    SimpleChat(
                        chatId = entity.chatId,
                        chatName = entity.chatName,
                        chatPicture = entity.chatPicture,
                        lastChatMessage = message.defaultTextMessage,
                        isUnread = message.isUnread,
                        lastSentTime = message.sentDate
                    )
                }
            }
    }

    override suspend fun getMessagesFlow(chatId: String, cacheManager: MessageCacheManager): Flow<PagingData<Message>> {
        return MessagesPager(chatId, database, cacheManager, apiService).getPager().flow
            .map { pagingData -> pagingData.map { it.toMessage() } }
    }
}
