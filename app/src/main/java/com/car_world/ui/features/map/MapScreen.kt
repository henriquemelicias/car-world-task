package com.car_world.ui.features.map

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.car_world.ui.theme.CarworldTheme
import ovh.plrapps.mapcompose.ui.MapUI

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),

) {
    MapUI(modifier, state = viewModel.state)
}

@Preview
@Composable
private fun MapScreenPreview() {
    CarworldTheme {
        Surface {
            MapScreen()
        }
    }
}