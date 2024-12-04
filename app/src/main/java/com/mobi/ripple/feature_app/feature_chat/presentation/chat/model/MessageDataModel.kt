package com.mobi.ripple.feature_app.feature_chat.presentation.chat.model

data class MessageDataModel(
    val text: String,
    val fileName: String? = null,
    val fileExtension: String? = null
)
