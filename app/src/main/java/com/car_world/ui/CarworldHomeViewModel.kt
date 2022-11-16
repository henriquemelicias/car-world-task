package com.example.car_world

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CarworldHomeViewModel: ViewModel() {
    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(CardworldHomeUIState(loading = true))
    val uiState: StateFlow<CardworldHomeUIState> = _uiState
}

data class CardworldHomeUIState(
    val loading: Boolean = false,
    val error: String? = null
)