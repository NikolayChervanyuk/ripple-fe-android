package com.mobi.ripple.feature_app.feature_chat.domain.repository

import androidx.paging.PagingData
import com.mobi.ripple.core.domain.common.Response
import com.mobi.ripple.feature_app.feature_chat.domain.model.Message
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChat
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun findUsersLike(
        queryText: String
    ): Response<List<SimpleChatUser>?>

    suspend fun createNewChat(chatName: String?, participantIds: List<String>): Response<SimpleChat?>

    suspend fun getChatUser(userId: String): Response<SimpleChatUser?>

    suspend fun hasPendingMessages(): Response<Boolean?>

    suspend fun getChatParticipants(chatId: String): List<SimpleChatUser>?
    suspend fun getChat(chatId: String): SimpleChat

    suspend fun getChatsFlow(cacheManager: MessageCacheManager): Flow<PagingData<SimpleChat>>

    suspend fun getMessagesFlow(chatId: String, cacheManager: MessageCacheManager): Flow<PagingData<Message>>
}