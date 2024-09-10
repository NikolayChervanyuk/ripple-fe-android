package com.mobi.ripple.core.data.data_source.remote.profile

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.data_source.local.profile.SimplePostEntity
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
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(
                endOfPaginationReached = true
            )

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    1
                } else {
                    state.pages.size + 1
                }
            }
        }
        //GlobalAppManager.storedUsername?.let { storedUsername ->
        val apiResponse = apiService.getSimplePosts(
            username = username,
            page = loadKey.toLong() - 1 //api paging starts from 0
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

        //} ?: return MediatorResult.Error(StoredUsernameNotFoundException())
    }


}