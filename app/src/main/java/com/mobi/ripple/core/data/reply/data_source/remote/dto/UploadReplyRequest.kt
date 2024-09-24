package com.mobi.ripple.core.data.reply.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadReplyRequest(
    val reply: String
)
