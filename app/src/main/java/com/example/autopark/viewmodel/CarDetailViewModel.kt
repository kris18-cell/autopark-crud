package com.example.autopark.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.autopark.data.entity.Car
import com.example.autopark.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarDetailViewModel(
    private val repository: CarRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _car = MutableStateFlow<Car?>(null)
    val car: StateFlow<Car?> = _car.asStateFlow()

    private val _brand = MutableStateFlow("")
    val brand: StateFlow<String> = _brand.asStateFlow()

    private val _model = MutableStateFlow("")
    val model: StateFlow<String> = _model.asStateFlow()

    private val _year = MutableStateFlow("")
    val year: StateFlow<String> = _year.asStateFlow()

    private val _licensePlate = MutableStateFlow("")
    val licensePlate: StateFlow<String> = _licensePlate.asStateFlow()

    private val _mileage = MutableStateFlow("")
    val mileage: StateFlow<String> = _mileage.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack.asStateFlow()

    init {
        val carId = savedStateHandle.get<Long>("carId") ?: -1
        if (carId != -1L) {
            loadCar(carId)
        }
    }

    private fun loadCar(carId: Long) {
        viewModelScope.launch {
            try {
                val loadedCar = repository.getCarById(carId)
                _car.value = loadedCar
                loadedCar?.let { car ->
                    _brand.value = car.brand
                    _model.value = car.model
                    _year.value = car.year.toString()
                    _licensePlate.value = car.licensePlate
                    _mileage.value = car.mileage.toString()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Помилка при завантаженні авто: ${e.localizedMessage}"
            }
        }
    }

    fun updateBrand(value: String) {
        _brand.value = value
    }

    fun updateModel(value: String) {
        _model.value = value
    }

    fun updateYear(value: String) {
        _year.value = value
    }

    fun updateLicensePlate(value: String) {
        _licensePlate.value = value
    }

    fun updateMileage(value: String) {
        _mileage.value = value
    }

    fun saveCar() {
        viewModelScope.launch {
            try {
                // Validate input
                if (_brand.value.isBlank() || _model.value.isBlank() || _licensePlate.value.isBlank()) {
                    _errorMessage.value = "Заповніть всі обов'язкові поля"
                    return@launch
                }

                val yearValue = _year.value.toIntOrNull()
                if (yearValue == null || yearValue < 1900 || yearValue > 2100) {
                    _errorMessage.value = "Введіть коректний рік"
                    return@launch
                }

                val mileageValue = _mileage.value.toIntOrNull()
                if (mileageValue == null || mileageValue < 0) {
                    _errorMessage.value = "Введіть коректний пробіг"
                    return@launch
                }

                val carToSave = Car(
                    id = _car.value?.id ?: 0,
                    brand = _brand.value,
                    model = _model.value,
                    year = yearValue,
                    licensePlate = _licensePlate.value,
                    mileage = mileageValue
                )

                if (carToSave.id == 0L) {
                    repository.insertCar(carToSave)
                } else {
                    repository.updateCar(carToSave)
                }

                _navigateBack.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Помилка при збереженні авто: ${e.localizedMessage}"
            }
        }
    }

    fun resetNavigateBack() {
        _navigateBack.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}

class CarDetailViewModelFactory(
    private val repository: CarRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarDetailViewModel(repository, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}