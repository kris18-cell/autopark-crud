package com.example.autopark

import android.app.Application
import com.example.autopark.data.database.AppDatabase
import com.example.autopark.data.repository.CarRepository

class AutoParkApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CarRepository(database.carDao()) }
}