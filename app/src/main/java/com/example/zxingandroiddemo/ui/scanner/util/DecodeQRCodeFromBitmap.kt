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
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true)

    val intArray = IntArray(resizedBitmap.width * resizedBitmap.height)
    resizedBitmap.getPixels(intArray, 0, resizedBitmap.width, 0, 0, resizedBitmap.width, resizedBitmap.height)

    val source = RGBLuminanceSource(resizedBitmap.width, resizedBitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val reader = MultiFormatReader().apply {
            setHints(
                mapOf(
                    DecodeHintType.POSSIBLE_FORMATS to formats,
                    DecodeHintType.TRY_HARDER to true
                )
            )
        }

        val result = reader.decode(binaryBitmap)
        result.text
    } catch (e: Exception) {
        Log.d("QRCodeDecode", "Failed: ${e.message}")
        null
    }
}