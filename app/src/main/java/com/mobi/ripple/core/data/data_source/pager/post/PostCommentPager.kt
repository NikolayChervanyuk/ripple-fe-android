package com.mobi.ripple.core.data.data_source.pager.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobi.ripple.core.config.ConstraintValues
import com.mobi.ripple.core.data.data_source.pager.PagerHolder
import com.mobi.ripple.core.data.data_source.remote.post.PostApiService
import com.mobi.ripple.core.data.data_source.remote.post.dto.PostCommentResponse
import com.mobi.ripple.core.exceptions.ApiResponseException

class PostCommentPager(
    private var apiService: PostApiService,
    private var postId: String
): PagerHolder<Int, PostCommentResponse> {
    override fun getPager(): Pager<Int, PostCommentResponse> {

        return Pager(
            config = PagingConfig(pageSize = ConstraintValues.POST_COMMENT_PAGE_SIZE),
            pagingSourceFactory = {CommentsPagingSource(postId, apiService)}
        )
    }

    private class CommentsPagingSource(
        private val postId: String,
        private val apiService: PostApiService
    ): PagingSource<Int, PostCommentResponse>() {
        override fun getRefreshKey(state: PagingState<Int, PostCommentResponse>) = 0

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostCommentResponse> {
            val loadKey =  params.key ?: 0
            val response = apiService.getPostComments(postId,  loadKey)
            if (!response.isError) {
                return LoadResult.Page(
                    data = response.content!!,
                    prevKey = if (loadKey <= 0) 0 else (loadKey - 1),
                    nextKey = loadKey + 1
                )
            }
            return LoadResult.Error(ApiResponseException(response.httpStatusCode))
        }
    }
}