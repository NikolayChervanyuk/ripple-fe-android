package com.mobi.ripple.core.data.comment.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditCommentRequest(
    val comment: String
)
