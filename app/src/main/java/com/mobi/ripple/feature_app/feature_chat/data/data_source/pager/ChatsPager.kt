package com.mobi.ripple.feature_app.feature_chat.data.data_source.pager

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobi.ripple.core.config.ConstraintValues.Companion.CHATS_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.common.PagerHolder
import com.mobi.ripple.feature_app.feature_chat.data.data_source.local.ChatEntity
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.ChatApiService
import com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.ChatsRemoteMediator
import com.mobi.ripple.feature_app.feature_chat.service.util.MessageCacheManager

class ChatsPager(
    val database: AppDatabase,
    private val messageCacheManager: MessageCacheManager,
    val apiService: ChatApiService
) : PagerHolder<Int, ChatEntity> {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPager(): Pager<Int, ChatEntity> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(
                pageSize = CHATS_PAGE_SIZE,
                prefetchDistance = CHATS_PAGE_SIZE,
                maxSize = 3 * CHATS_PAGE_SIZE,
                jumpThreshold = CHATS_PAGE_SIZE,
                enablePlaceholders = true
            ),
            remoteMediator = ChatsRemoteMediator(
                appDb = database,
                messageCacheManager = messageCacheManager,
                apiService = apiService
            ),
            pagingSourceFactory = {database.chatDao.pagingSource()}
        )
    }
}