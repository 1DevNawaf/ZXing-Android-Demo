package com.example.zxingandroiddemo.ui.scanner.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

@OptIn(ExperimentalGetImage::class)
fun processImageProxy(
    imageProxy: ImageProxy,
    formats: List<BarcodeFormat>,
    onQRCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: return imageProxy.close()

    val buffer = mediaImage.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    val source = PlanarYUVLuminanceSource(
        bytes,
        mediaImage.width,
        mediaImage.height,
        0, 0,
        mediaImage.width,
        mediaImage.height,
        false
    )
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    try {
        val reader = MultiFormatReader().apply {
            setHints(
                mapOf(
                    DecodeHintType.POSSIBLE_FORMATS to formats,
                    DecodeHintType.TRY_HARDER to true
                )
            )
        }

        val result = reader.decode(binaryBitmap)
        Handler(Looper.getMainLooper()).post {
            onQRCodeScanned(result.text)
        }
    } catch (e: Exception) {
        Log.d("QRCodeScannerView", "No barcode found: ${e.message}")
    } finally {
        imageProxy.close()
    }
}