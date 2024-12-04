package com.mobi.ripple.core.util.paging

interface PageFactories<T> {
    val remoteLoadFactory: suspend (pageIndex: Int) -> PagedData<T>?
    val cacheUpdateFactory: suspend (pageIndex: Int, itemIndexOnPage: Int) -> Boolean
    val cacheFetchFactory: suspend (pageIndex: Int) -> PagedData<T>
}