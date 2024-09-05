package com.mobi.ripple.feature_app.feature_search.presentation.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.mobi.ripple.core.presentation.components.SimpleUserItemModel
import com.mobi.ripple.core.util.BitmapUtils
import com.mobi.ripple.feature_app.feature_search.domain.model.SimpleUser

data class SimpleUserModel(
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

fun SimpleUser.asSimpleUserModel() = SimpleUserModel(
    id = id,
    fullName = fullName,
    username = username,
    isActive = isActive,
    pfp = profilePicture?.let {
        BitmapUtils
            .convertImageByteArrayToBitmap(it)
            .asImageBitmap()
    }
)