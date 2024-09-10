package com.mobi.ripple.core.presentation.profile.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.feature_app.feature_profile.domain.model.UserProfileSimplePost
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileSimplePostModel(
    val id: String,
    val image: ImageBitmap,
    val authorId: String
)

fun UserProfileSimplePost.asUserProfileSimplePostModel() = UserProfileSimplePostModel(
    id = id,
    image = BitmapUtils.convertImageByteArrayToBitmap(image).asImageBitmap(),
    authorId = authorId
)