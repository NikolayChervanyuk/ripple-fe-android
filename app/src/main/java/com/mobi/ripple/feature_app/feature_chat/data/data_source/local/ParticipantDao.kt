package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ParticipantDao {

    @Upsert
    suspend fun upsertAll(participants: List<ParticipantEntity>)

    @Upsert
    suspend fun upsert(participant: ParticipantEntity)

    @Query("SELECT p.* FROM participantentity AS p " +
            "JOIN participantchatentity AS pc ON pc.participantId = p.id " +
            "WHERE pc.chatId = :chatId")
    suspend fun getAllByChatId(chatId: String): List<ParticipantEntity>

    @Query("SELECT * FROM participantentity " +
            "WHERE id = :userId")
    suspend fun getById(userId: String): ParticipantEntity
}