package com.mobi.ripple.core.data.data_source.pager.profile

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobi.ripple.core.config.ConstraintValues
import com.mobi.ripple.core.data.data_source.local.AppDatabase
import com.mobi.ripple.core.data.data_source.local.profile.SimplePostEntity
import com.mobi.ripple.core.data.data_source.pager.PagerHolder
import com.mobi.ripple.core.data.data_source.remote.profile.ProfileApiService
import com.mobi.ripple.core.data.data_source.remote.profile.SimplePostRemoteMediator

class SimplePostPager(
    private var apiService: ProfileApiService,
    private var database: AppDatabase,
    private var username: String
): PagerHolder<Int, SimplePostEntity> {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPager(): Pager<Int, SimplePostEntity> {
        return Pager(
            config = PagingConfig(pageSize = ConstraintValues.SIMPLE_POST_PAGE_SIZE),
            remoteMediator = SimplePostRemoteMediator(
                appDb = database,
                apiService = apiService,
                username = username
            ),
            pagingSourceFactory = { database.simplePostDao.pagingSource() }
        )

    }
}