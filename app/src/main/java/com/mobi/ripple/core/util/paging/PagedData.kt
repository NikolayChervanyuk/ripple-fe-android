package com.mobi.ripple.core.util.paging

class PagedData<T>(
    private val data: Pair<Int, MutableList<T>>,
    val onUpdate: suspend (itemIndexOnPage: Int) -> Boolean
) {

    fun getItems(): List<T> {
        return data.second
    }

    fun getIndex() = data.first

    suspend fun update(newItem: T, indexOnPage: Int) {
        data.second[indexOnPage] = newItem
        onUpdate(indexOnPage)
    }

    fun isEmpty(): Boolean {
        return data.second.isEmpty()
    }

    fun <R> map(transformer: (element: T) -> R): PagedData<R> {
        return PagedData(
            data = Pair(
                data.first,
                data.second.map { element -> transformer.invoke(element) }.toMutableList()
            ),
            onUpdate = onUpdate
        )
    }

    companion object {
        fun <T> empty(): PagedData<T> {
            return PagedData(Pair(-1, mutableListOf<T>())) { false }
        }
    }
}

fun <T> MutableList<T>.asPagedData(
    pageIndex: Int,
    onUpdate: suspend (itemIndexOnPage: Int) -> Boolean
) = PagedData(Pair(pageIndex, this), onUpdate)