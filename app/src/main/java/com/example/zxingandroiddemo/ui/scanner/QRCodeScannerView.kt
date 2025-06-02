package com.example.zxingandroiddemo.ui.scanner

import android.Manifest
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.zxingandroiddemo.ui.home.HomeViewModel
import com.example.zxingandroiddemo.ui.scanner.util.decodeQRCodeFromBitmap
import com.example.zxingandroiddemo.ui.scanner.util.processImageProxy
import com.google.zxing.BarcodeFormat
import java.io.InputStream
import java.util.concurrent.Executors

@Composable
fun QRCodeScannerView(
    viewModel: HomeViewModel,
    onScanned: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember { mutableStateOf(false) }
    val alreadyScanned = remember { mutableStateOf(false) }
    val supportedFormats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_128)


    // Gallery picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val result = decodeQRCodeFromBitmap(bitmap,supportedFormats)
            if (result != null) {
                viewModel.updateScannedText(result)
                onScanned()
            } else {
                Toast.makeText(context, "No QR code found in image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasPermission = granted }
    )

    // Request permission on first load
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val previewView = remember { PreviewView(context) }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setImageAnalysisAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                if (alreadyScanned.value) {
                    imageProxy.close()
                    return@setImageAnalysisAnalyzer
                }
                processImageProxy(imageProxy, supportedFormats) { scannedText ->
                    alreadyScanned.value = true
                    this.unbind() // Stop camera
                    viewModel.updateScannedText(scannedText)
                    Handler(Looper.getMainLooper()).postDelayed({
                        onScanned()
                    }, 200)
                }
            }
        }
    }

    // Bind camera when permission is granted
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            previewView.controller = cameraController
            cameraController.bindToLifecycle(lifecycleOwner)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (hasPermission) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            // Show fallback UI when permission is denied
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Camera permission denied.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }) {
                        Text("Try Again")
                    }
                }
            }
        }

        // Always show gallery scan option
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                imagePickerLauncher.launch("image/*")
            }) {
                Text("Scan from Gallery")
            }
        }
    }
}