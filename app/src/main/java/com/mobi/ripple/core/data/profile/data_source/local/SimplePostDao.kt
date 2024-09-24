package com.mobi.ripple.core.data.profile.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SimplePostDao {

    @Upsert
    suspend fun upsertAll(simplePosts: List<SimplePostEntity>)

    @Query("SELECT * FROM simplepostentity " +
            "ORDER BY creationDate DESC")
    fun pagingSource(): PagingSource<Int, SimplePostEntity>

    @Query("Delete FROM simplepostentity")
    suspend fun clearAll()
}