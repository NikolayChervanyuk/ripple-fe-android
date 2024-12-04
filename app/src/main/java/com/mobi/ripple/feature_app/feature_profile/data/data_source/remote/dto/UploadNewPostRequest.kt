package com.mobi.ripple.feature_app.feature_profile.data.data_source.remote.dto

import com.mobi.ripple.core.domain.profile.model.UserProfileNewPost
import kotlinx.serialization.Serializable

@Serializable
data class UploadNewPostRequest(
    val imageBytes: ByteArray,
    val captionText: String
)

fun UserProfileNewPost.asUploadPostRequest() = UploadNewPostRequest(
    imageBytes = imageBytes,
    captionText = captionText
)