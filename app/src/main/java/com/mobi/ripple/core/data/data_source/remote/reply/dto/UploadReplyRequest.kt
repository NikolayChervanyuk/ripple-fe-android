package com.mobi.ripple.core.data.data_source.remote.reply.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadReplyRequest(
    val reply: String
)