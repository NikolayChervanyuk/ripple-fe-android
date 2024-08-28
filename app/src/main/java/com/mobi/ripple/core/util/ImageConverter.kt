package com.mobi.ripple.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun convertImageByteArrayToBitmap(imageData: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
}