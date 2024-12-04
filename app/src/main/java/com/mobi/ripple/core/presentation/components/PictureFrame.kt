package com.mobi.ripple.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mobi.ripple.R
import com.mobi.ripple.core.theme.BrightGreen

@Composable
fun PictureFrame(
    modifier: Modifier = Modifier,
    picture: ImageBitmap?,
    placeHolderDrawableId: Int = R.drawable.user_profile_btn,
//    placeHolderTint: Color = MaterialTheme.colorScheme.outlineVariant,
    borderWidthDp: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    isActive: Boolean = false
) {
    val imageModifier = Modifier
        .fillMaxSize()
        .zIndex(1f)
        .border(
            width = borderWidthDp,
            color = borderColor,
            shape = CircleShape
        )
        .clip(CircleShape)
        .padding(innerPadding)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        ActivePin(show = isActive)
        picture?.let {
            Image(
                modifier = imageModifier,
                bitmap = picture,
                contentScale = ContentScale.Crop,
                contentDescription = "user profile picture"
            )
        } ?: Icon(
            modifier = imageModifier.background(color = Color.White),
//            tint = placeHolderTint,
            painter = painterResource(id = placeHolderDrawableId),
            contentDescription = "user profile picture"
        )
    }
}

@Composable
private fun ActivePin(show: Boolean) {
    if (show) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = CircleShape

                )
                .size(11.dp)
                .padding(1.dp)
                .clip(CircleShape)

                .background(BrightGreen)
                .zIndex(Float.MAX_VALUE)
        )
    }
}