package com.example.autopark.data.dao

import androidx.room.*
import com.example.autopark.data.entity.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY brand ASC")
    fun getAllCars(): Flow<List<Car>>

    @Query("SELECT * FROM cars WHERE id = :carId")
    suspend fun getCarById(carId: Long): Car?

    @Insert
    suspend fun insertCar(car: Car): Long

    @Update
    suspend fun updateCar(car: Car)

    @Delete
    suspend fun deleteCar(car: Car)
}