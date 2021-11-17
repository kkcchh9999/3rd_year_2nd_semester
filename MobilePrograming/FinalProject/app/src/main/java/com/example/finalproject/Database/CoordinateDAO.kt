package com.example.finalproject.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalproject.data.Coordinate

@Dao
interface CoordinateDAO {
    //시도코드로 데이터 가져오는 쿼리
    @Query("SELECT * FROM Coordinate WHERE siDo=(:siDo)")
    fun getCoordinates(siDo: Int): LiveData<List<Coordinate>>

    @Insert    //데이터 입력 쿼리
    fun insertCoordinate(coordinate: Coordinate)
}