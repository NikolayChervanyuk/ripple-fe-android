package com.mobi.ripple.core.data.data_source.pager.posts

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobi.ripple.core.config.ConstraintValues
import com.mobi.ripple.core.data.data_source.pager.PagerHolder
import com.mobi.ripple.core.data.data_source.remote.post.PostApiService
import com.mobi.ripple.core.data.data_source.remote.post.dto.PostResponse
import com.mobi.ripple.core.exceptions.ApiResponseException

class PostsPager(
    private var apiService: PostApiService,
    private var authorId: String,
    private var startIndex: Int
) : PagerHolder<Int, PostResponse> {
    override fun getPager(): Pager<Int, PostResponse> {
        return Pager(
            config = PagingConfig(pageSize = ConstraintValues.POSTS_PAGE_SIZE),
            pagingSourceFactory = {
                PostsPagingSource(
                    apiService = apiService,
                    authorId = authorId,
                    startIndex = startIndex
                )
            }
        )
    }

    private class PostsPagingSource(
        private var apiService: PostApiService,
        private var authorId: String,
        private var startIndex: Int
    ) : PagingSource<Int, PostResponse>() {
        override fun getRefreshKey(state: PagingState<Int, PostResponse>): Int {
            return startIndex / ConstraintValues.POSTS_PAGE_SIZE
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostResponse> {
            val loadKey = params.key ?: 0
            val response = apiService.getPosts(authorId, loadKey)
            if (!response.isError) {
                return LoadResult.Page(
                    data = response.content!!,
                    prevKey = if(loadKey == 0) null else loadKey - 1,
                    nextKey = if(response.content.isEmpty()) null else loadKey + 1
                )
            }
            return LoadResult.Error(ApiResponseException(response.httpStatusCode))
        }
    }
}