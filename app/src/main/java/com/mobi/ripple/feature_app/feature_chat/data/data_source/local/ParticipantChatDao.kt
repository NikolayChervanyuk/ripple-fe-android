package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ParticipantChatDao {
    @Upsert
    suspend fun upsert(participantChatEntity: ParticipantChatEntity)

    @Upsert
    suspend fun upsertAll(participantChatEntities: List<ParticipantChatEntity>)


    @Query("SELECT * FROM participantchatentity " +
            "WHERE chatId = :chatId")
    suspend fun getByChatId(chatId: String): List<ParticipantChatEntity>

    @Delete
    suspend fun remove(participantChatEntity: ParticipantChatEntity)
}