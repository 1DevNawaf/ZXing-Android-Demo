package com.example.zxingandroiddemo.ui.scanner.util

import android.graphics.Bitmap

fun resizeBitmap(bitmap: Bitmap, maxSize: Int = 1024): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val scale = maxSize.toFloat() / maxOf(width, height)

    return if (scale < 1f) {
        Bitmap.createScaledBitmap(
            bitmap,
            (width * scale).toInt(),
            (height * scale).toInt(),
            true
        )
    } else {
        bitmap
    }
}