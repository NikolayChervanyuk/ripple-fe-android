package com.mobi.ripple.core.data.reply.data_source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.reply.data_source.local.ReplyEntity
import com.mobi.ripple.core.exceptions.ApiResponseException

@OptIn(ExperimentalPagingApi::class)
class ReplyRemoteMediator(
    private val database: AppDatabase,
    private val apiService: ReplyApiService,
    private val postId: String,
    private val commentId: String
) : RemoteMediator<Int, ReplyEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReplyEntity>
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
        val apiResponse = apiService.getLatestReplies(postId, commentId, loadKey)

        if (!apiResponse.isError) {
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.replyDao.clearAll()
                }
                val replyEntities = apiResponse.content!!.map { it.asReplyEntity() }
                database.replyDao.upsertAll(replyEntities)
            }
            return MediatorResult.Success(
                endOfPaginationReached = apiResponse.content!!.isEmpty()
            )
        }
        return MediatorResult.Error(ApiResponseException(apiResponse.httpStatusCode))
    }
}