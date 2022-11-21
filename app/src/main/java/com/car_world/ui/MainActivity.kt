package com.car_world.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.car_world.ui.layout.CarworldBottomSheetScaffold
import com.car_world.ui.theme.CarworldTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CarworldTheme {
                Surface {
                    CarworldBottomSheetScaffold()
                }
            }

        }
    }
}