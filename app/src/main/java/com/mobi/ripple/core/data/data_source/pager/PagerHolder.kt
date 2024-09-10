package com.mobi.ripple.core.data.data_source.pager

import androidx.paging.Pager
import com.mobi.ripple.core.data.data_source.local.profile.SimplePostEntity

interface PagerHolder<Key: Any, Value: Any> {
    fun getPager(): Pager<Key, Value>
}