package com.sanket.mvvmstructure.ui.state

import com.sanket.mvvmstructure.databased.dao.Station

sealed class StationUiState {
    object Loading : StationUiState()
    data class Success(val stations: List<Station>) : StationUiState()
    data class Error(val message: String) : StationUiState()
}