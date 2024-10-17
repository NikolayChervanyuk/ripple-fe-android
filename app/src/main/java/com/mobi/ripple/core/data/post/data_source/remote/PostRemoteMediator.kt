package com.mobi.ripple.core.data.post.data_source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mobi.ripple.core.config.ConstraintValues.Companion.POSTS_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.post.data_source.local.PostEntity
import com.mobi.ripple.core.exceptions.ApiResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val appDb: AppDatabase,
    private val apiService: PostApiService,
    private val authorId: String,
    private val startItemIndex: Int
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        val loadKey: Int = when (loadType) {
            LoadType.REFRESH -> startItemIndex / POSTS_PAGE_SIZE
            LoadType.PREPEND -> {
                var firstLoadedPage: Int
                withContext(Dispatchers.IO) {
                    firstLoadedPage = appDb.postDao.getFirst()?.page ?: 0
                }
                if (firstLoadedPage == 0) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                } else firstLoadedPage - 1
            }

            LoadType.APPEND -> {
                withContext(Dispatchers.IO) {
                    (appDb.postDao.getLast()?.page?.plus(1)) ?: 0
                }
            }
        }

        val apiResponse = apiService.getPosts(authorId, loadKey)

        if (!apiResponse.isError) {
            if (apiResponse.content!!.isEmpty()) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            appDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDb.postDao.clearAll()
                }
                val postEntities = apiResponse.content.map {
                    val entity = it.asPostEntity()
                    entity.page = loadKey
                    entity
                }
                appDb.postDao.upsertAll(postEntities)
            }

            return MediatorResult.Success(endOfPaginationReached = false)
        }
        return MediatorResult.Error(ApiResponseException(apiResponse.httpStatusCode))
    }
}