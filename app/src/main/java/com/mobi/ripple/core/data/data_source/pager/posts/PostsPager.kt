package com.mobi.ripple.core.data.data_source.pager.posts

import androidx.paging.Pager
import com.mobi.ripple.core.data.data_source.pager.PagerHolder
import com.mobi.ripple.core.data.data_source.remote.post.PostApiService
import com.mobi.ripple.core.data.data_source.remote.post.dto.PostResponse

class PostsPager(
    private var apiService: PostApiService,
    private var authorId: String,
    private var startIndex: Int
): PagerHolder<Int, PostResponse> {
    override fun getPager(): Pager<Int, PostResponse> {
        TODO("Not yet implemented")
    }

    private class PostsPagingSource(

    )
}