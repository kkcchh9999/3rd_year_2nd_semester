package com.example.finalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity //데이터베이스 엔터티, 시도코드, 위도, 경도를 가짐
data class Coordinate(
    var siDo: Int,
    @PrimaryKey
    var latitude: Double,
    var longitude: Double
)
