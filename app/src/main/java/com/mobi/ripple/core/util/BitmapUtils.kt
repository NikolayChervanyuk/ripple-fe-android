package com.mobi.ripple.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

class BitmapUtils {
    companion object {
        fun convertImageByteArrayToBitmap(imageData: ByteArray): Bitmap {
            val maxTargetSize = 4_600_000
            var sampleSize = 1
            var sizeForSample = imageData.size
            while (sizeForSample > maxTargetSize) {
                sizeForSample /= 4
                sampleSize *= 2
            }

            val options = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)
        }

        fun getExifRotation(context: Context, uri: Uri): Int {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val exif = ExifInterface(inputStream)
                return when (exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    else -> 0
                }
            }
            return 0
        }

        fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
            if (degrees == 0) return bitmap
            val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
            return Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.width, bitmap.height, matrix, true)
        }
    }
}
