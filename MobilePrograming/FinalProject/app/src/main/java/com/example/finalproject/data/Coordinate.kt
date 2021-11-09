package com.example.finalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Coordinate(
    var siDo: Int,
    @PrimaryKey
    var latitude: Double,
    var longitude: Double
)
