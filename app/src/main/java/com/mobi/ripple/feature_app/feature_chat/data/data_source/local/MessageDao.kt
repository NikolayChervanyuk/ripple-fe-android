package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import java.time.Instant

@Dao
interface MessageDao {

    @Upsert
    suspend fun upsertMessage(messageEntity: MessageEntity)

    @Insert
    suspend fun insertMessages(messageEntities: List<MessageEntity>)

    @Query(
        "SELECT * FROM messageentity " +
                "WHERE chatId = :chatId " +
                "ORDER BY sentDate DESC " +
                "LIMIT 1"
    )
    suspend fun getLatestMessageByChatId(chatId: String): MessageEntity

    @Query(
        "SELECT EXISTS(" +
                "SELECT * FROM messageentity " +
                "WHERE chatId = :chatId" +
                ")"
    )
    suspend fun hasChatMessages(chatId: String): Boolean

    @Query(
        "SELECT * FROM messageentity " +
                "WHERE chatId = :chatId " +
                "ORDER BY sentDate DESC"
    )
    fun pagingSource(chatId: String): PagingSource<Int, MessageEntity>

    @Query(
        "SELECT COUNT(*) FROM messageentity " +
                "WHERE isUnread = 1"
    )
    suspend fun getUnreadMessagesCount(): Int

    @Query("UPDATE messageentity SET isUnread = 0 WHERE chatId = :chatId")
    suspend fun markAsRead(chatId: String)

}