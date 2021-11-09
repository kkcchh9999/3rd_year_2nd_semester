package com.example.finalproject.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalproject.data.Coordinate

//데이터베이스
@Database(entities = [Coordinate::class], version = 1)
abstract class CoordinateDatabase : RoomDatabase() {
    abstract fun CoordinateDAO(): CoordinateDAO
}