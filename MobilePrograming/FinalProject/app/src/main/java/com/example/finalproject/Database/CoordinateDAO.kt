package com.example.finalproject.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalproject.data.Coordinate

@Dao
interface CoordinateDAO {

    @Query("SELECT * FROM Coordinate WHERE siDo=(:siDo)")
    fun getCoordinates(siDo: Int): LiveData<List<Coordinate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)    //충돌시 교체
    fun insertCoordinate(coordinate: Coordinate)

    @Insert
    fun insertTestCase(coordinate: Coordinate)
}