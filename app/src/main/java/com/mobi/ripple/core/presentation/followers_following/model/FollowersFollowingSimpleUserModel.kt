package com.mobi.ripple.core.presentation.followers_following.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.domain.followers_following.model.FollowersFollowingSimpleUser
import com.mobi.ripple.core.presentation.components.SimpleUserItemModel
import com.mobi.ripple.core.util.BitmapUtils

data class FollowersFollowingSimpleUserModel(
    val id: String,
    val fullName: String?,
    val username: String,
    val isActive: Boolean,
    val pfp: ImageBitmap?
) {

    fun asSimpleUserItemModel() = SimpleUserItemModel(
        id = id,
        fullName = fullName,
        username = username,
        isActive = isActive,
        profilePicture = pfp
    )
}

fun FollowersFollowingSimpleUser.asFollowersFollowingSimpleUserModel() =
    FollowersFollowingSimpleUserModel(
        id = id,
        fullName = fullName,
        username = username,
        isActive = isActive,
        pfp = profilePicture?.let {
            BitmapUtils.convertImageByteArrayToBitmap(it).asImageBitmap()
        }
    )