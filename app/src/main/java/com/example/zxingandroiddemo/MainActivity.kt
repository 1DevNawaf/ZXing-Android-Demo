package com.example.zxingandroiddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zxingandroiddemo.ui.home.HomeView
import com.example.zxingandroiddemo.ui.home.HomeViewModel
import com.example.zxingandroiddemo.ui.scanner.QRCodeScannerView
import com.example.zxingandroiddemo.ui.theme.ZXingAndroidDemoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZXingAndroidDemoTheme {
                AppNavigator()
            }
        }
    }
}


@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val viewModel = remember { HomeViewModel() }

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeView(
                scannedText = viewModel.scannedText,
                onStartScan = { navController.navigate("scanner") },
                onClear = { viewModel.clear() }
            )
        }

        composable("scanner") {
            QRCodeScannerView(
                viewModel = viewModel,
                onScanned = {
                    navController.popBackStack()
                }
            )
        }
    }
}