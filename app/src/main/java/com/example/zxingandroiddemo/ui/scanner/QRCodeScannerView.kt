package com.example.zxingandroiddemo.ui.scanner

import android.Manifest
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.zxingandroiddemo.ui.home.HomeViewModel
import com.example.zxingandroiddemo.ui.scanner.util.processImageProxy
import java.util.concurrent.Executors

@Composable
fun QRCodeScannerView(
    viewModel: HomeViewModel,
    onScanned: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasPermission = granted }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!hasPermission) {
        Text("Camera permission is required.")
        return
    }

    val previewView = remember { PreviewView(context) }
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setImageAnalysisAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                processImageProxy(imageProxy) { scannedText ->
                    updateScannedTextAndNavigate(
                        scannedText,
                        viewModel,
                        this,
                        onScanned
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        previewView.controller = cameraController
        cameraController.bindToLifecycle(lifecycleOwner)
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
}

private fun updateScannedTextAndNavigate(
    scannedText: String,
    viewModel: HomeViewModel,
    controller: LifecycleCameraController,
    onScanned: () -> Unit
) {
    controller.unbind()
    viewModel.updateScannedText(scannedText)
    Handler(Looper.getMainLooper()).postDelayed({
        onScanned()
    }, 200)
}