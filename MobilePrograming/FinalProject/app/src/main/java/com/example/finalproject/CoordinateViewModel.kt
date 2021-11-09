package com.example.finalproject

import androidx.lifecycle.ViewModel
import com.example.finalproject.Database.CoordinateRepository
import com.example.finalproject.data.Coordinate

class CoordinateViewModel : ViewModel() {

    private val coordinateRepository = CoordinateRepository.get()

    fun insertCoordinate(coordinate: Coordinate) =
        coordinateRepository.insertCoordinate(coordinate)

//    val testJeju = coordinateRepository.getCoordinate(50)
    fun getCoordinate(siDo: Int) =
        coordinateRepository.getCoordinate(siDo)
}