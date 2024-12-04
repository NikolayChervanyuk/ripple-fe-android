package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mobi.ripple.GlobalAppManager
import com.mobi.ripple.core.exceptions.ApiResponseException
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.MessageEntity
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageResolver
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
class MessagesRemoteMediator(
    private val chatId: String,
    private val cacheManager: MessageCacheManager,
    private val apiService: ChatApiService
) : RemoteMediator<Int, MessageEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
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

        val apiResponse = apiService.getMessages(chatId, loadKey)
        if(apiResponse.isError) {
            return MediatorResult.Error(ApiResponseException(apiResponse.httpStatusCode))
        }

        coroutineScope {
            launch {
                if (cacheManager.database.messageDao.hasChatMessages(chatId)) {
                    val latestLocalMessageSentDate =
                        cacheManager.database.messageDao.getLatestMessageByChatId(chatId).sentDate
                    for (message in apiResponse.content!!) {
                        if (latestLocalMessageSentDate.isBefore(message.sentDate)){
                            val genericMessage = message.asGenericMessage()
                            cacheManager.cache(
                                message = genericMessage,
                                isMine = MessageResolver.getSenderId(genericMessage) == GlobalAppManager.storedId,
                                isUnread = false
                            )
                        }
                    }
                } else {
                    for (message in apiResponse.content!!) {
                        val genericMessage = message.asGenericMessage()
                        cacheManager.cache(
                            message = genericMessage,
                            isMine = MessageResolver.getSenderId(genericMessage) == GlobalAppManager.storedId,
                            isUnread = false
                        )
                    }
                }
            }
        }
        return MediatorResult.Success(endOfPaginationReached = apiResponse.content!!.isEmpty())
    }
}