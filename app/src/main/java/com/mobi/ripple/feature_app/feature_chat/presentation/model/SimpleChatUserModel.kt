package com.mobi.ripple.feature_app.feature_chat.presentation.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.presentation.components.SimpleUserItemModel
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.feature_app.feature_chat.domain.model.SimpleChatUser

data class SimpleChatUserModel(
    val userId: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val userPfp: ImageBitmap?
)

fun SimpleUserItemModel.asSimpleChatUserModel() = SimpleChatUserModel(
    userId = id,
    fullName = fullName,
    username = username,
    isActive = isActive,
    userPfp = profilePicture
)

fun SimpleChatUser.asSimpleChatUserModel() = SimpleChatUserModel(
    userId = id,
    fullName = fullName,
    username = username,
    isActive = active,
    userPfp = userPfp?.let { BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap() }
)

fun SimpleChatUser.asSimpleUserItemModel() = SimpleUserItemModel(
    id = id,
    fullName = fullName,
    username = username,
    isActive = active,
    profilePicture = userPfp?.let { BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap() }
)
