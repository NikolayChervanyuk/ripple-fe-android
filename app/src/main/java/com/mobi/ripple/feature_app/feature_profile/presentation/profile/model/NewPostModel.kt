package com.mobi.ripple.feature_app.feature_profile.presentation.profile.model

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileNewPost

data class NewPostModel(
    var imageBytes: ByteArray,
    var captionText: String
) {

    fun asUserProfileNewPost() = UserProfileNewPost(
        imageBytes = imageBytes,
        captionText = captionText
    )
}