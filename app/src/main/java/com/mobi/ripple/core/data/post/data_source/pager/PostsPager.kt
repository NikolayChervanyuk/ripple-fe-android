package com.mobi.ripple.core.data.post.data_source.pager

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobi.ripple.core.config.ConstraintValues
import com.mobi.ripple.core.config.ConstraintValues.Companion.POSTS_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.common.PagerHolder
import com.mobi.ripple.core.data.post.data_source.local.PostEntity
import com.mobi.ripple.core.data.post.data_source.remote.PostApiService
import com.mobi.ripple.core.data.post.data_source.remote.PostRemoteMediator
import com.mobi.ripple.core.data.post.data_source.remote.dto.PostResponse
import com.mobi.ripple.core.exceptions.ApiResponseException

class PostsPager(
    private var apiService: PostApiService,
    private var database: AppDatabase,
    private var authorId: String,
    private var startItemIndex: Int
) : PagerHolder<Int, PostEntity> {
    @OptIn(ExperimentalPagingApi::class)
    override fun getPager(): Pager<Int, PostEntity> {
        return Pager(
            initialKey = startItemIndex,
            config = PagingConfig(
                pageSize = POSTS_PAGE_SIZE,
                prefetchDistance = POSTS_PAGE_SIZE,
                maxSize = 4 * POSTS_PAGE_SIZE,
                jumpThreshold = 3,
                enablePlaceholders = true
            ),
            remoteMediator = PostRemoteMediator(
                appDb = database,
                apiService = apiService,
                authorId = authorId,
                startItemIndex = startItemIndex
            ),
            pagingSourceFactory = { database.postDao.pagingSource() }
        )

//        return Pager(
//            config = PagingConfig(pageSize = ConstraintValues.POSTS_PAGE_SIZE),
//            pagingSourceFactory = {
//                PostsPagingSource(
//                    apiService = apiService,
//                    authorId = authorId,
//                    startIndex = startIndex
//                )
//            }
//        )
    }

    private class PostsPagingSource(
        private var apiService: PostApiService,
        private var authorId: String,
        private var startIndex: Int
    ) : PagingSource<Int, PostResponse>() {

        override val keyReuseSupported: Boolean
            get() = true

        override val jumpingSupported: Boolean
            get() = true

        override fun getRefreshKey(state: PagingState<Int, PostResponse>): Int {
            return startIndex / ConstraintValues.POSTS_PAGE_SIZE
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostResponse> {
            val loadKey = params.key ?: 0
            val response = apiService.getPosts(authorId, loadKey)
            if (!response.isError) {
                return LoadResult.Page(
                    data = response.content!!,
                    prevKey = if (loadKey == 0) null else loadKey - 1,
                    nextKey = if (response.content.isEmpty()) null else loadKey + 1
                )
            }
            return LoadResult.Error(ApiResponseException(response.httpStatusCode))
        }
    }
}