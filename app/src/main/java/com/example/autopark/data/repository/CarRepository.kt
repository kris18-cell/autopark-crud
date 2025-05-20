package com.example.autopark.data.repository

import com.example.autopark.data.dao.CarDao
import com.example.autopark.data.entity.Car
import kotlinx.coroutines.flow.Flow

class CarRepository(private val carDao: CarDao) {
    val allCars: Flow<List<Car>> = carDao.getAllCars()

    suspend fun getCarById(id: Long): Car? {
        return carDao.getCarById(id)
    }

    suspend fun insertCar(car: Car): Long {
        return carDao.insertCar(car)
    }

    suspend fun updateCar(car: Car) {
        carDao.updateCar(car)
    }

    suspend fun deleteCar(car: Car) {
        carDao.deleteCar(car)
    }
}