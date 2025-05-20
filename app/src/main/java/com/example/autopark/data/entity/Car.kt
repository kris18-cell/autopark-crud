// Car.kt
package com.example.autopark.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val mileage: Int
)