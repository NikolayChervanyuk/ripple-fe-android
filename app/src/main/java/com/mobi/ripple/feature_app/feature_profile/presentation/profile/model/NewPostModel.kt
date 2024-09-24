package com.mobi.ripple.feature_app.feature_profile.presentation.profile.model

import com.mobi.ripple.core.domain.profile.model.UserProfileNewPost

data class NewPostModel(
    var imageBytes: ByteArray,
    var captionText: String
) {

    fun asUserProfileNewPost() = UserProfileNewPost(
        imageBytes = imageBytes,
        captionText = captionText
    )
}