package com.mobi.ripple.feature_app.feature_chat.data.data_source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParticipantChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val participantId: String,
    val chatId: String
)
