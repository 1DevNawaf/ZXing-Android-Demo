package com.example.zxingandroiddemo.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.zxingandroiddemo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(scannedText: String, onStartScan: () -> Unit, onClear: () -> Unit) {
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color(0xFF7593dc),
                titleContentColor = Color.White,
            ),
            title = { Text("QR Code Scanner") }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = onStartScan) {
                Text("Start QR Scan")
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (scannedText.isNotEmpty()) {
                Log.d("QRCodeScannerView","Scanned Result: ".plus(scannedText))
                Text("Scanned Result:")
                OutlinedTextField(
                    value = scannedText,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black),
                    trailingIcon = {
                        IconButton(onClick = {
                            clipboardManager.setText(AnnotatedString(scannedText))
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_content_copy),
                                contentDescription = "Copy",
                                tint = Color.Black
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onClear) {
                    Text("Clear")
                }
            }
        }
    }
}