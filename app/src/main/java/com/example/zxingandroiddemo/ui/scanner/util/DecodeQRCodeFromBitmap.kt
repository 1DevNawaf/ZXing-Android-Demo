package com.example.zxingandroiddemo.ui.scanner.util

import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer


fun decodeQRCodeFromBitmap(
    bitmap: Bitmap,
    formats: List<BarcodeFormat>
): String? {
    val safeBitmap = resizeBitmap(bitmap, 1000)

    val width = safeBitmap.width
    val height = safeBitmap.height
    val pixels = IntArray(width * height)
    safeBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    val source = RGBLuminanceSource(width, height, pixels)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val reader = MultiFormatReader().apply {
            setHints(
                mapOf(
                    DecodeHintType.POSSIBLE_FORMATS to formats.ifEmpty { BarcodeFormat.entries },
                    DecodeHintType.TRY_HARDER to true
                )
            )
        }
        reader.decode(binaryBitmap).text
    } catch (e: Exception) {
        Log.e("QRCodeDecode", "Failed to decode: ${e.message}")
        null
    }
}