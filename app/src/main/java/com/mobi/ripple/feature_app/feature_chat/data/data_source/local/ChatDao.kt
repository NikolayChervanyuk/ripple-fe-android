package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.mobi.ripple.core.data.post.data_source.local.PostEntity

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chatEntity: ChatEntity)

    @Upsert
    suspend fun upsertAllChats(chatEntities: List<ChatEntity>)

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Query("SELECT * FROM chatentity " +
            "WHERE chatId = :chatId")
    suspend fun getChatById(chatId: String): ChatEntity

    @Query("SELECT * FROM chatentity " +
            "ORDER BY lastSentMessageTime DESC")
    fun pagingSource(): PagingSource<Int, ChatEntity>


}