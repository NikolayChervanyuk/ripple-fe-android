package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.exceptions.ApiResponseException
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ChatEntity
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageResolver
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class ChatsRemoteMediator(
    private val appDb: AppDatabase,
    private val messageCacheManager: MessageCacheManager,
    private val apiService: ChatApiService
) : RemoteMediator<Int, ChatEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatEntity>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> {
                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) 0
                else state.pages.lastIndex + 1
            }
        }
        val chatsApiResponse = apiService.getChats(loadKey)

        if (chatsApiResponse.isError) {
            Timber.e("mediator error!!!")
            return MediatorResult.Error(ApiResponseException(chatsApiResponse.httpStatusCode))
        }
        val chats = chatsApiResponse.content!!
            .map { it.asChatEntity() }
            .ifEmpty { return MediatorResult.Success(endOfPaginationReached = true) }
        try {
            appDb.withTransaction {
                coroutineScope {
                    for (chat in chats) {
                        if (!appDb.messageDao.hasChatMessages(chat.chatId)) {
                            launch {
                                val chatMessagesResponse =
                                    apiService.getMessages(chat.chatId, 0)
                                if (chatMessagesResponse.isError) {
                                    throw ApiResponseException(
                                        chatsApiResponse.httpStatusCode,
                                        "Failed to get messages for chat ${chat.chatName}; " +
                                                "API error message: ${chatsApiResponse.errorMessage}"
                                    )
                                }
                                for (message in chatMessagesResponse.content!!) {
                                    val genericMessage = message.asGenericMessage()
                                    messageCacheManager.cache(
                                        message = genericMessage,
                                        isMine = MessageResolver.getSenderId(genericMessage) ==
                                                GlobalAppManager.storedId,
                                        isUnread = false
                                    )
                                }
                            }
                        }
                    }
                }
                appDb.chatDao.upsertAllChats(chats)
            }

        } catch (ex: ApiResponseException) {
            return MediatorResult.Error(ex)
        }
        return MediatorResult.Success(endOfPaginationReached = chats.isEmpty())
    }
}