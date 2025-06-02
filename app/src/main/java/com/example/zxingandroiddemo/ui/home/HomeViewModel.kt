package com.example.zxingandroiddemo.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var scannedText by mutableStateOf("")
        private set

    fun updateScannedText(text: String) {
        scannedText = text
    }

    fun clear() {
        scannedText = ""
    }
}