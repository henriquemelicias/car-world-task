package com.car_world.ui.features.cars_list

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.car_world.R
import com.car_world.core.cars.entities.CarModel
import com.car_world.ui.theme.CarworldTheme
import com.car_world.ui.theme.elevationLevel3

/**
 * Composable of a list of cars.
 */
@Composable
fun CarsListScreen(
    modifier: Modifier = Modifier,
    viewModel: CarsListViewModel = hiltViewModel(),
    onGotoMapMarkerButton: (carId: String) -> Unit
) {
    // Get cars from viewModel.
    val viewState by remember(viewModel) { viewModel.loadCarsList() }
        .collectAsState(initial = CarsListUIState())

    // Display cars list.
    LazyColumn(modifier = modifier) {
        items(viewState.carsList) {
            CarEntry(it, onGotoMapMarkerButton = onGotoMapMarkerButton)
        }
    }

}

/**
 * An entry on the list of cars.
 */
@Composable
fun CarEntry(car: CarModel, onGotoMapMarkerButton: (carId: String) -> Unit) {

    // Expand car entry details.
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .padding(all = 8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {

            // Car entry image.
            CarEntryImage(
                imageUrl = car.carImageUrl,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Title, subtitle and buttons.
            CarEntryBody(
                car,
                onDetailsButtonClick = { isExpanded = !isExpanded },
                onGotoMapMarkerButton = { onGotoMapMarkerButton(car.id) }
            )
        }

        // Expandable entry details.
        if (isExpanded) {
            CarEntryDetails(car)
        }
    }
}

@Composable
fun CarEntryImage(imageUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier
            .size(80.dp)
            .background(MaterialTheme.colorScheme.onSurface, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .border(1.5.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp)),

        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .error( R.drawable.ic_car_foreground)
            .build(),

        contentDescription = null,
    )
}

@Composable
fun CarEntryBody(
    car: CarModel,
    onDetailsButtonClick: () -> Unit,
    onGotoMapMarkerButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Title and toggle details button.
    Column(
        modifier = modifier.width(IntrinsicSize.Max)
    ) {

        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            val entryTitle = car.make + " " + car.modelName
            val entrySubtitle = " from " + car.name

            // Entry title.
            Text(
                text = car.make + " " + car.modelName,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Subtitle (don't show if too many characters).
            if ( entryTitle.length + entrySubtitle.length < 23)
            {
                Text(
                    modifier = Modifier
                        .absoluteOffset(y = (-2).dp)
                        .padding(horizontal = 4.dp),

                    text = " from " + car.name,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Buttons.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {

            Button(
                modifier = Modifier
                    .padding(vertical = 3.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = onDetailsButtonClick,
            ) {
                Text(
                    text = "Toggle Details"
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))

            // Goto marker button.
            Button(
                modifier = Modifier.padding(bottom = 3.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = onGotoMapMarkerButton,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_baseline_arrow_forward_ios_24),
                    contentDescription = "Goto marker",
                )
            }
        }

    }
}

@Composable
fun CarEntryDetails(car: CarModel) {
    Surface(
        modifier = Modifier
            .animateContentSize()
            .padding(1.dp)
            .fillMaxWidth()
            .width(IntrinsicSize.Max),
        shape = MaterialTheme.shapes.small,
        tonalElevation = elevationLevel3,
        color = MaterialTheme.colorScheme.surface,

        ) {
        Column {
            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            CarEntryDetailEntry("Owner", car.name )
            CarEntryDetailEntry("Inner Cleanliness", car.innerCleanliness)
            CarEntryDetailEntry("License Plate", car.licensePlate)
            CarEntryDetailEntry("Color", car.color)
            CarEntryDetailEntry("Maker", car.make)
            CarEntryDetailEntry("Group", car.group)
            CarEntryDetailEntry("Series", car.series)
            CarEntryDetailEntry("Fuel Type", "${car.fuelType}")
            CarEntryDetailEntry("Fuel Level", "${car.fuelLevel}")
            CarEntryDetailEntry("Transmission", "${car.transmission}")
            CarEntryDetailEntry("Latitude", "${car.latitude}")
            CarEntryDetailEntry("Longitude", "${car.longitude}")

            Spacer(modifier = Modifier.padding(vertical = 5.dp))
        }

    }
}

@Composable
fun CarEntryDetailEntry(
    textCategory: String,
    textCategoryContent: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .width(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,

    ) {
        // Category.
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 1.dp)
                .fillMaxWidth(0.5f),
            text = "$textCategory:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        // Category Content.
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 1.dp),
            text = textCategoryContent,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun CarEntryPreview() {
    CarworldTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {

            val car = CarModel(
                id = "WMWSW31030T222518",
                modelIdentifier = "mini",
                modelName = "MINI",
                name = "Vanessa",
                make = "BMW",
                group = "MINI",
                color = "midnight_black",
                series = "MINI",
                fuelType = 'D',
                fuelLevel = 0.7F,
                transmission = 'M',
                licensePlate = "M-VO0259",
                latitude = 48.134557,
                longitude = 11.576921,
                innerCleanliness = "REGULAR",
                carImageUrl = "https://cdn.sixt.io/codingtask/images/mini.png"
            )
            CarEntry(car, onGotoMapMarkerButton = {})

        }
    }
}