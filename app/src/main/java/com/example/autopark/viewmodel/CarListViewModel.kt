package com.example.autopark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autopark.data.entity.Car
import com.example.autopark.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarListViewModel(private val repository: CarRepository) : ViewModel() {
    val cars: StateFlow<List<Car>> = repository.allCars
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                repository.deleteCar(car)
            } catch (e: Exception) {
                _errorMessage.value = "Помилка при видаленні авто: ${e.localizedMessage}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

class CarListViewModelFactory(private val repository: CarRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}