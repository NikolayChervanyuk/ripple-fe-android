package com.mobi.ripple.core.data.data_source.remote.comment.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditCommentRequest(
    val comment: String
)
