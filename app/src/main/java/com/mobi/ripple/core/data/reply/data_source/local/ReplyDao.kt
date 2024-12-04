package com.mobi.ripple.core.data.reply.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ReplyDao {
    @Upsert
    suspend fun upsertAll(replies: List<ReplyEntity>)

    @Query(
        "SELECT * FROM replyentity " +
                "ORDER BY createdDate DESC"
    )
    fun pagingSource(): PagingSource<Int, ReplyEntity>

    @Query("Delete FROM replyentity")
    suspend fun clearAll()
}