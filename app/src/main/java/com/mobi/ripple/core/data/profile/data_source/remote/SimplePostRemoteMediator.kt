package com.mobi.ripple.core.data.profile.data_source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.profile.data_source.local.SimplePostEntity
import com.mobi.ripple.core.exceptions.ApiResponseException

@OptIn(ExperimentalPagingApi::class)
class SimplePostRemoteMediator(
    private val appDb: AppDatabase,
    private val apiService: ProfileApiService,
    private var username: String
) : RemoteMediator<Int, SimplePostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SimplePostEntity>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(
                endOfPaginationReached = true
            )

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) 0
                else state.pages.lastIndex + 1
            }
        }

        val apiResponse = apiService.getSimplePosts(
            username = username,
            page = loadKey.toLong()
        )
        if (!apiResponse.isError) {
            appDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDb.simplePostDao.clearAll()
                }
                val simplePostEntities = apiResponse.content!!.map { it.asSimplePostEntity() }
                appDb.simplePostDao.upsertAll(simplePostEntities)
            }

            return MediatorResult.Success(
                endOfPaginationReached = apiResponse.content!!.isEmpty()
            )
        }
        return MediatorResult.Error(ApiResponseException(apiResponse.httpStatusCode))
    }


}