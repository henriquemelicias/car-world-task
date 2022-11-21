package com.car_world.ui.features.cars_list

import androidx.lifecycle.ViewModel
import com.car_world.core.cars.entities.CarModel
import com.car_world.infrastructure.cars.interfaces.ICarsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class CarsListUIState(
    val error: String? = null,
    var carsList: List<CarModel> = listOf()
)

@HiltViewModel
class CarsListViewModel @Inject constructor(
    private val carsRepo: ICarsRepo
): ViewModel() {

    fun loadCarsList(): Flow<CarsListUIState> = flow {
        emit(CarsListUIState(null, carsRepo.getCars(true)))
    }
}
