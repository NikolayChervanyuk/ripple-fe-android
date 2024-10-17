package com.mobi.ripple.feature_app.feature_chat.data.data_source.pager

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobi.ripple.core.config.ConstraintValues.Companion.MESSAGES_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.common.PagerHolder
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.MessageEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.ChatApiService
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.MessagesRemoteMediator
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager

class MessagesPager(
    private val chatId: String,
    private val database: AppDatabase,
    private val cacheManager: MessageCacheManager,
    private val apiService: ChatApiService
) : PagerHolder<Int, MessageEntity> {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPager(): Pager<Int, MessageEntity> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(
                maxSize = 5 * MESSAGES_PAGE_SIZE,
                pageSize = MESSAGES_PAGE_SIZE,
                prefetchDistance = 2 * MESSAGES_PAGE_SIZE,
                jumpThreshold = MESSAGES_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = MessagesRemoteMediator(
                chatId = chatId,
                cacheManager = cacheManager,
                apiService = apiService
            ),
            pagingSourceFactory = { database.messageDao.pagingSource(chatId) }
        )
    }

}