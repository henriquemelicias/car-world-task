package com.example.car_world

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.car_world.ui.utils.CarworldContentType
import com.car_world.ui.utils.CarworldNavigationType
import com.car_world.ui.utils.DevicePosture
import com.example.reply.ui.CarworldDestinations
import kotlinx.coroutines.launch

@Composable
fun CarworldApp(
    carworldHomeUIState: CardworldHomeUIState,
    windowSize: WindowWidthSizeClass,
    foldingDevicePosture: DevicePosture
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: CarworldNavigationType
    val contentType: CarworldContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = CarworldNavigationType.BOTTOM_NAVIGATION
            contentType = CarworldContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = CarworldNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture is DevicePosture.BookPosture
                || foldingDevicePosture is DevicePosture.Separating) {
                CarworldContentType.LIST_AND_DETAIL
            } else {
                CarworldContentType.LIST_ONLY
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                CarworldNavigationType.NAVIGATION_RAIL
            } else {
                CarworldNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = CarworldContentType.LIST_AND_DETAIL
        }
        else -> {
            navigationType = CarworldNavigationType.BOTTOM_NAVIGATION
            contentType = CarworldContentType.LIST_ONLY
        }
    }

    CarworldNavigationWrapperUI(navigationType, contentType, carworldHomeUIState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarworldNavigationWrapperUI(
    navigationType: CarworldNavigationType,
    contentType: CarworldContentType,
    carworldHomeUIState: CardworldHomeUIState
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedDestination = CarworldDestinations.INBOX

    if ( navigationType == CarworldNavigationType.PERMANENT_NAVIGATION_DRAWER ) {
        PermanentNavigationDrawer(drawerContent = { NavigationDrawerContent(selectedDestination)}) {
            CarworldAppContent(navigationType, contentType, carworldHomeUIState)
        }
    }
    else {
        ModalNavigationDrawer(
            drawerContent = {
                NavigationDrawerContent(
                    selectedDestination,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            CarworldAppContent(
                navigationType,
                contentType,
                carworldHomeUIState,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }

    CarworldAppContent(navigationType, contentType, carworldHomeUIState)
}


@Composable
fun CarworldAppContent(
    navigationType: CarworldNavigationType,
    contentType: CarworldContentType,
    carworldHomeUIState: CardworldHomeUIState,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == CarworldNavigationType.NAVIGATION_RAIL) {
            CarworldNavigationRail(
                onDrawerClicked = onDrawerClicked
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            /*
            if (contentType == ReplyContentType.LIST_AND_DETAIL) {
                ReplyListAndDetailContent(
                    replyHomeUIState = replyHomeUIState,
                    modifier = Modifier.weight(1f),
                )
            } else {
                ReplyListOnlyContent(replyHomeUIState = replyHomeUIState, modifier = Modifier.weight(1f))
            }
            */

            AnimatedVisibility(visible = navigationType == CarworldNavigationType.BOTTOM_NAVIGATION) {
                CarworldBottomNavigationBar()
            }
        }
    }
}

@Composable
@Preview
fun CarworldNavigationRail(
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        NavigationRailItem(
            selected = false,
            onClick = onDrawerClicked,
            icon =  { Icon(imageVector = Icons.Default.Menu, contentDescription = "TEMP") }
        )
        NavigationRailItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon =  { Icon(imageVector = Icons.Default.Menu, contentDescription = "TEMP") }
        )
        NavigationRailItem(
            selected = false,
            onClick = {/*TODO*/ },
            icon =  { Icon(imageVector = Icons.Default.Menu, contentDescription = "TEMP") }
        )
        NavigationRailItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon =  { Icon(imageVector = Icons.Outlined.Menu, contentDescription = "TEMP") }
        )
        NavigationRailItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon =  { Icon(imageVector = Icons.Outlined.Menu, contentDescription = "TEMP") }
        )
    }
}

@Composable
@Preview
fun CarworldBottomNavigationBar() {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Default.Menu, contentDescription = "TEMP") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Default.Menu, contentDescription = "TEMP") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Outlined.Menu, contentDescription = "TEMP") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(imageVector = Icons.Outlined.Menu, contentDescription = "TEMP") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    selectedDestination: String,
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {}
) {
    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(24.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TEMP".uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onDrawerClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "TEMP"
                )
            }
        }

        NavigationDrawerItem(
            selected = selectedDestination == CarworldDestinations.INBOX,
            label = { Text(text = "TEMP", modifier = Modifier.padding(horizontal = 16.dp)) },
            icon = { Icon(imageVector = Icons.Default.Menu, contentDescription =  "TEMP") },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            selected = selectedDestination == CarworldDestinations.ARTICLES,
            label = { Text(text = "TEMP", modifier = Modifier.padding(horizontal = 16.dp)) },
            icon = { Icon(imageVector =  Icons.Default.Menu, contentDescription =  "TEMP") },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            selected = selectedDestination == CarworldDestinations.DM,
            label = { Text(text = "TEMP", modifier = Modifier.padding(horizontal = 16.dp)) },
            icon = { Icon(imageVector =  Icons.Default.Menu, contentDescription =  "TEMP") },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
        NavigationDrawerItem(
            selected = selectedDestination == CarworldDestinations.GROUPS,
            label = { Text(text = "TMEP", modifier = Modifier.padding(horizontal = 16.dp)) },
            icon = { Icon(imageVector =  Icons.Default.Menu, contentDescription =  "TEMP") },
            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
            onClick = { /*TODO*/ }
        )
    }
}
