package com.car_world.ui.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.car_world.ui.features.cars_list.CarsListScreen
import com.car_world.ui.features.map.MapScreen
import com.car_world.ui.features.map.MapViewModel
import com.car_world.ui.theme.elevationLevel3
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CarworldBottomSheetScaffold(
    viewModel: MapViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Swipe up to expand")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                CarsListScreen( onGotoMapMarkerButton = {
                    viewModel.onCenter(it, 1f)
                    scope.launch {
                        scaffoldState.bottomSheetState.collapse()
                    }
                })
            }
        },
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("") },
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = elevationLevel3,
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Localized description")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.absoluteOffset( y = (-50).dp),
                onClick = {
                    viewModel.onRotate(0f, increment = false)
                }
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Rotates map to its default position")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 70.dp,
        drawerContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CarsListScreen( onGotoMapMarkerButton = {
                    viewModel.onCenter(it, 1f)
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
            }
        }
    ) {
        MapScreen()
    }
}


