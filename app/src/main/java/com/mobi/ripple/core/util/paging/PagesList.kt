package com.mobi.ripple.core.util.paging

import kotlinx.coroutines.runBlocking

class PagesList<T>(
    private val startPageIndex: Int,
    private val pageLoad: suspend (index: Int) -> PagedData<T>?,
    inMemoryPages: Int = 3,
) {

    private var isInit = false

    var currentIndex = startPageIndex
    private var beforeRange =
        startPageIndex - inMemoryPages / 2 - if (inMemoryPages % 2 == 0) 1 else 0
    private var afterRange = startPageIndex + inMemoryPages / 2

    var endIndex: Int? = null

    private val pagesList = MutableList<PagedData<T>?>(startPageIndex + 1) { null }

    private suspend fun initList() {
        var beforeBound = startPageIndex - beforeRange
        if (beforeBound < 0) beforeBound = 0
        while (beforeBound < startPageIndex) {
            pagesList[beforeBound] = pageLoad.invoke(beforeBound++)
        }
        pagesList[startPageIndex] = pageLoad.invoke(startPageIndex)

        var afterBound = startPageIndex + 1
        while (afterBound <= startPageIndex + afterRange) {
            val page = pageLoad.invoke(afterBound + 1)
            if (page != null) {
                if (page.isEmpty()) {
                    endIndex = afterBound
                    break
                } else {
                    pagesList.add(++afterBound, page)
                }
            }
        }
    }

    suspend fun getCurrent(): PagedData<T>? {
        if (!isInit) initList()
        return pagesList[currentIndex]
    }

    suspend fun getPrevious(): PagedData<T>? {
        if (!isInit) initList()
        if (currentIndex - 1 < 0) {
            throw IllegalArgumentException("Previous page requested, but current one is the first")
        }

        val bound = currentIndex - beforeRange - 1
        if (bound >= 0) {
            pagesList[bound] = pageLoad.invoke(bound)
            if (pagesList.lastIndex >= currentIndex + afterRange) {
                pagesList[currentIndex + afterRange] = null
            }
        }
        return pagesList[--currentIndex]
    }

    suspend fun getNext(): PagedData<T>? {
        if (!isInit) initList()
        if (endIndex != null && currentIndex + 1 > endIndex!!) {
            return PagedData.empty()
        } else if (currentIndex + 1 > pagesList.lastIndex) {
            val newPage = pageLoad.invoke(currentIndex + 1)
            if (newPage != null) {
                if (newPage.isEmpty()) {
                    endIndex = currentIndex
                } else {
                    pagesList.add(newPage)
                }
            }
        }
        pagesList[currentIndex - beforeRange] = null
        return pagesList[++currentIndex]
    }

    fun getSize(): Int {
        if (!isInit) {
            runBlocking { initList() }
        }
        return pagesList.size
    }

    fun isEmpty(): Boolean {
        return pagesList.size == 0
    }

    companion object {
        fun <T> emptyList(): PagesList<T> {
            return PagesList(-1, { null }, 0)
        }
    }
}