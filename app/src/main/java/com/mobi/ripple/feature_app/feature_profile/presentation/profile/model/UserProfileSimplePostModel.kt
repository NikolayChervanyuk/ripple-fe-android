package com.mobi.ripple.feature_app.feature_profile.presentation.profile.model

import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost

data class UserProfileSimplePostModel(
    val id: String,
    val image: ByteArray?
)

fun UserProfileSimplePost.asUserProfileSimplePostModel() =
    UserProfileSimplePostModel(id = id, image = image)