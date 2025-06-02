package com.example.zxingandroiddemo.ui.scanner.util

import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

fun decodeQRCodeFromBitmap(bitmap: Bitmap): String? {
    val resizedBitmap = resizeBitmap(bitmap)

    val intArray = IntArray(resizedBitmap.width * resizedBitmap.height)
    resizedBitmap.getPixels(
        intArray, 0, resizedBitmap.width, 0, 0,
        resizedBitmap.width, resizedBitmap.height
    )

    val source = RGBLuminanceSource(resizedBitmap.width, resizedBitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        MultiFormatReader().decode(binaryBitmap).text
    } catch (e: Exception) {
        Log.d("QRCodeScannerView", "Decoding failed: ${e.message}")
        null
    }
}