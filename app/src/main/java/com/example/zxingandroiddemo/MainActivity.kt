package com.example.zxingandroiddemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.zxingandroiddemo.ui.scanner.QRCodeScannerView
import com.example.zxingandroiddemo.ui.theme.ZXingAndroidDemoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZXingAndroidDemoTheme {
                QRCodeScannerView(onQRCodeScanned = {
                    Toast.makeText(this, "QR Code: $it", Toast.LENGTH_LONG).show()
                })
            }
        }
    }
}