package com.mobi.ripple.feature_app.feature_chat.data.data_source.remote.http.dto

import kotlinx.serialization.Serializable

@Serializable
data class NewChatRequest(
    val name: String?,
    val userToAddIds: List<String>
)
