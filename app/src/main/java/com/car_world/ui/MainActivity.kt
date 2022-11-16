package com.car_world.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.car_world.CardworldHomeUIState
import com.example.car_world.CarworldApp
import com.example.car_world.CarworldHomeViewModel
import com.example.car_world.ui.theme.CarworldTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CarworldHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        setContent {
            CarworldTheme {
                val windowSize = calculateWindowSizeClass(this)
                val uiState = viewModel.uiState.collectAsState().value
                CarworldApp(uiState, windowSize.widthSizeClass)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarworldAppPreview() {
    CarworldTheme {
        CarworldApp(
            uiState = CardworldHomeUIState(),
            windowWidth = WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun CarworldAppPreviewTablet() {
    CarworldTheme {
        CarworldApp(
            uiState = CardworldHomeUIState(),
            windowWidth = WindowWidthSizeClass.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun CarworldAppPreviewDesktop() {
    CarworldTheme {
        CarworldApp(
            uiState = CardworldHomeUIState(),
            windowWidth = WindowWidthSizeClass.Expanded
        )
    }
}
