package com.mobi.ripple.feature_app.feature_profile.presentation.profile.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter

@Composable
fun SimplePostItem(imageData: ByteArray?, onClicked: () -> Unit) {
    if (imageData != null) {
        val imageBitmap =
            BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap()
        Image(
            modifier = Modifier
                .aspectRatio(3f / 4f)
                .fillMaxWidth()
                .clickable { onClicked() },
            painter = BitmapPainter(imageBitmap),
            contentDescription = "a post"
        )
    } else {
        Box(
            modifier = Modifier
                .aspectRatio(3f / 4f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Can't load image",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}