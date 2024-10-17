package com.mobi.ripple.feature_app.feature_chat.domain.model

data class SimpleChatUser(
    val id: String,
    val fullName: String?,
    val username: String,
    var active: Boolean,
    val userPfp: ByteArray?
)
