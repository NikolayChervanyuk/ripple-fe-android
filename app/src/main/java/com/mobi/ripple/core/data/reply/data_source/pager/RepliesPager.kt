package com.mobi.ripple.core.data.reply.data_source.pager

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobi.ripple.core.config.ConstraintValues.Companion.REPLIES_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.common.PagerHolder
import com.mobi.ripple.core.data.reply.data_source.local.ReplyEntity
import com.mobi.ripple.core.data.reply.data_source.remote.ReplyApiService
import com.mobi.ripple.core.data.reply.data_source.remote.ReplyRemoteMediator

class RepliesPager(
    private val appDb: AppDatabase,
    private val apiService: ReplyApiService,
    private val postId: String,
    private val commentId: String
) : PagerHolder<Int, ReplyEntity> {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPager(): Pager<Int, ReplyEntity> {
        return Pager(
            config = PagingConfig(pageSize = REPLIES_PAGE_SIZE),
            initialKey = 0,
            remoteMediator = ReplyRemoteMediator(
                database = appDb,
                apiService = apiService,
                postId = postId,
                commentId = commentId
            ),
            pagingSourceFactory = { appDb.replyDao.pagingSource() }
        )
    }
}