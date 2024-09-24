package com.mobi.ripple.core.data.common

import androidx.paging.Pager

interface PagerHolder<Key: Any, Value: Any> {
    fun getPager(): Pager<Key, Value>
}