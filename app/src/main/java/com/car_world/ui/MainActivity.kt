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
import com.car_world.ui.layout.HomeUIState
import com.car_world.ui.layout.HomeViewModel
import com.car_world.ui.theme.CarworldTheme
import com.car_world.ui.utils.*

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Flow to emit every time there's a change in the windowLayout.
        val devicePostureFlow = onWindowLayoutChangeFlow(this)

        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        setContent {
            CarworldTheme {
                val uiState = viewModel.uiState.collectAsState().value
                val windowSize = calculateWindowSizeClass(this)
                val devicePosture = devicePostureFlow.collectAsState().value

                App(uiState, windowSize.widthSizeClass, devicePosture)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    CarworldTheme {
        App(
            homeUIState = HomeUIState(),
            windowSize = WindowWidthSizeClass.Compact,
            foldingDevicePosture = DevicePosture.NormalPosture
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun AppPreviewTablet() {
    CarworldTheme {
        App(
            homeUIState = HomeUIState(),
            windowSize = WindowWidthSizeClass.Medium,
            foldingDevicePosture = DevicePosture.NormalPosture
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun AppPreviewDesktop() {
    CarworldTheme {
        App(
            homeUIState = HomeUIState(),
            windowSize = WindowWidthSizeClass.Expanded,
            foldingDevicePosture = DevicePosture.NormalPosture
        )
    }
}
