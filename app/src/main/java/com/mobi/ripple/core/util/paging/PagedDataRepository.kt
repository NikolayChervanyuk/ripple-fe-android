package com.mobi.ripple.core.util.paging

class PagedDataRepository<T>(
    private val remoteLoadFactory: suspend (pageIndex: Int) -> PagedData<T>?,
    private val cacheUpdateFactory: suspend (pageIndex: Int, itemIndexOnPage: Int) -> Boolean,
    private val cacheFetchFactory: suspend (pageIndex: Int) -> PagedData<T>,
    private val cacheClearFactory: suspend () -> Unit
) {
    var loadState: PageLoadState = PageLoadState.Idle

    var isSourceEmpty = false

    var lastPageIndex: Int? = null

    var lastRequestedPage: Int? = null

    suspend fun getPage(pageIndex: Int): PagedData<T>? {
        lastPageIndex?.let { if (pageIndex > it) return PagedData.empty() }
        lastRequestedPage = pageIndex
        loadState = PageLoadState.Loading
        val localPage = cacheFetchFactory.invoke(pageIndex)
        if (localPage.getItems().isEmpty()) {
            val remotePage = remoteLoadFactory.invoke(pageIndex)
            if (remotePage == null) {
                loadState = PageLoadState.Error
                return null
            } else {
                loadState = PageLoadState.Success
                if (remotePage.getItems().isEmpty()) {
                    if (pageIndex == 0) {
                        isSourceEmpty = true
                        return PagedData.empty()
                    }
                    lastPageIndex = pageIndex
                }
                return remotePage
            }
        } else {
            loadState = PageLoadState.Success
            return localPage
        }
    }

    suspend fun updatePageItem(pageIndex: Int, itemOnPageIndex: Int): Boolean {
        return cacheUpdateFactory.invoke(pageIndex, itemOnPageIndex)
    }

    suspend fun clearCache() {
        cacheClearFactory.invoke()
    }

}

