package com.sanket.mvvmstructure.ui.fragment.mainFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sanket.mvvmstructure.data.repository.RepositoryStation
import com.sanket.mvvmstructure.ui.state.StationUiState
import com.sanket.mvvmstructure.unit.result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationViewModel @Inject constructor(private val repository: RepositoryStation) : ViewModel() {

    private val _uiState = MutableSharedFlow<StationUiState>(replay = 1)
    val uiState: SharedFlow<StationUiState> = _uiState.asSharedFlow()


    private val _location = MutableLiveData<LatLng?>()
    val location: MutableLiveData<LatLng?> = _location

    private val _title = MutableLiveData<String?>()
    val title :MutableLiveData<String?> = _title

    init {
        loadStations()
    }


    fun loadStations() {
        viewModelScope.launch {
            _uiState.emit(StationUiState.Loading)

            when (val result = repository.fetchStations()) {
                is result.success -> {
                    val stations = result.data ?: emptyList()
                    _uiState.emit(StationUiState.Success(stations))
                    _title.value = stations[0].n
                    _location.value = LatLng(stations[0].lt.toDouble(), stations[0].lg.toDouble())
                }
                is result.error -> {
                    val message = result.message ?: "Unknown error"
                    _uiState.emit(StationUiState.Error(message))
                }
            }
        }
    }
}
