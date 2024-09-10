package com.mobi.ripple.core.presentation.post.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.domain.model.post.PostSimpleUser
import com.mobi.ripple.core.util.BitmapUtils

data class PostSimpleUserModel(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val profilePicture: ImageBitmap?
)

fun PostSimpleUser.asPostSimpleUserModel() = PostSimpleUserModel(
    id = id,
    fullName = fullName,
    username = username,
    isActive = isActive,
    profilePicture = profilePicture?.let {
        BitmapUtils
            .convertImageByteArrayToBitmap(it)
            .asImageBitmap()
    }
)
