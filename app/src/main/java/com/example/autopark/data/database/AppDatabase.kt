package com.example.autopark.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.autopark.data.dao.CarDao
import com.example.autopark.data.entity.Car

@Database(entities = [Car::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "autopark_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}