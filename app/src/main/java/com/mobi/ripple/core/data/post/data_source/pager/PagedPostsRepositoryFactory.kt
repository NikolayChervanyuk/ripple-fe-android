package com.mobi.ripple.core.data.post.data_source.pager

import androidx.compose.ui.util.fastForEach
import com.mobi.ripple.core.config.ConstraintValues.Companion.POSTS_PAGE_SIZE
import com.mobi.ripple.core.data.common.AppDatabase
import com.mobi.ripple.core.data.post.data_source.remote.PostApiService
import com.mobi.ripple.core.domain.post.model.Post
import com.mobi.ripple.core.util.paging.PageFactories
import com.mobi.ripple.core.util.paging.PagedData
import com.mobi.ripple.core.util.paging.PagedDataRepository
import com.mobi.ripple.core.util.paging.asPagedData

class PagedPostsRepositoryFactory(
    private val authorId: String,
    private val postApiService: PostApiService,
    private val database: AppDatabase
) : PageFactories<Post> {

    override val remoteLoadFactory: suspend (pageIndex: Int) -> PagedData<Post>? =
        { pageIndex ->
            with(postApiService.getPosts(authorId, pageIndex)) {
                if (isError) return@with null
                database.postDao.upsertAll(content!!.map { it.asPostEntity() })
                content.map { it.asPost() }
                    .toMutableList()
                    .asPagedData(
                        pageIndex = pageIndex,
                        onUpdate = { itemIndexOnPage ->
                            cacheUpdateFactory.invoke(pageIndex, itemIndexOnPage)
                        }
                    )
            }
        }

    override val cacheUpdateFactory: suspend (pageIndex: Int, itemIndexOnPage: Int) -> Boolean =
        { pageIndex, itemIndexOnPage ->
            val localPost = database.postDao
                .getPost(pageIndex * POSTS_PAGE_SIZE + itemIndexOnPage)

            val apiResponse = postApiService.getPost(localPost.id)
            if (!apiResponse.isError) {
                database.postDao.updatePost(apiResponse.content!!.asPostEntity())

            }
            !apiResponse.isError
        }

    override val cacheFetchFactory: suspend (pageIndex: Int) -> PagedData<Post> = { pageIndex ->
        val mutableList = mutableListOf<Post>()
        database.postDao.getPage(pageIndex, POSTS_PAGE_SIZE)
            .fastForEach { mutableList.add(it.asPost()) }
        mutableList.asPagedData(
            pageIndex = pageIndex,
            onUpdate = { itemIndexOnPage -> cacheUpdateFactory.invoke(pageIndex, itemIndexOnPage) })
    }

    suspend fun create(): PagedDataRepository<Post> {
//        database.postDao.clearAll()
        return PagedDataRepository(
            remoteLoadFactory = { pageInd ->
                remoteLoadFactory.invoke(pageInd)
            },
            cacheUpdateFactory = cacheUpdateFactory,
            cacheFetchFactory = cacheFetchFactory,
            cacheClearFactory = suspend { database.postDao.clearAll() }
        )
    }
}