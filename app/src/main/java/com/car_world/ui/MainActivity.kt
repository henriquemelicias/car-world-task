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
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import com.car_world.ui.utils.DevicePosture
import com.car_world.ui.utils.isBookPosture
import com.car_world.ui.utils.isSeparating
import com.car_world.ui.utils.isTableTopPosture
import com.example.car_world.CardworldHomeUIState
import com.example.car_world.CarworldApp
import com.example.car_world.CarworldHomeViewModel
import com.example.car_world.ui.theme.CarworldTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainActivity : ComponentActivity() {

    private val viewModel: CarworldHomeViewModel by viewModels()

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

                CarworldApp(uiState, windowSize.widthSizeClass, devicePosture)
            }
        }
    }
}

private fun onWindowLayoutChangeFlow(activityInstance: MainActivity) = WindowInfoTracker
    .getOrCreate(activityInstance)
    .windowLayoutInfo(activityInstance)
    .flowWithLifecycle(activityInstance.lifecycle)
    .map { layoutInfo ->
        val foldingFeature = layoutInfo
            .displayFeatures
            .filterIsInstance<FoldingFeature>()
            .firstOrNull()
        when {
            isTableTopPosture(foldingFeature) ->
                DevicePosture.TableTopPosture(foldingFeature.bounds)

            isBookPosture(foldingFeature) ->
                DevicePosture.BookPosture(foldingFeature.bounds)

            isSeparating(foldingFeature) ->
                DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

            else -> DevicePosture.NormalPosture
        }
    }
    .stateIn(
        scope = activityInstance.lifecycleScope,
        started = SharingStarted.Eagerly,
        initialValue = DevicePosture.NormalPosture
    )

@Preview(showBackground = true)
@Composable
fun CarworldAppPreview() {
    CarworldTheme {
        CarworldApp(
            carworldHomeUIState = CardworldHomeUIState(),
            windowSize = WindowWidthSizeClass.Compact,
            foldingDevicePosture = DevicePosture.NormalPosture
        )
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun CarworldAppPreviewTablet() {
    CarworldTheme {
        CarworldApp(
            carworldHomeUIState = CardworldHomeUIState(),
            windowSize = WindowWidthSizeClass.Medium,
            foldingDevicePosture = DevicePosture.NormalPosture
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun CarworldAppPreviewDesktop() {
    CarworldTheme {
        CarworldApp(
            carworldHomeUIState = CardworldHomeUIState(),
            windowSize = WindowWidthSizeClass.Expanded,
            foldingDevicePosture = DevicePosture.NormalPosture
        )
    }
}
