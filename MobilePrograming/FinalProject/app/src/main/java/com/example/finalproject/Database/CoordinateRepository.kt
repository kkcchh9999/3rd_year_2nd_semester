package com.example.finalproject.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.finalproject.data.Coordinate
import java.lang.IllegalStateException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

const val DATABASE_NAME = "CoordinateDatabase"
class CoordinateRepository private constructor(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        CoordinateDatabase::class.java,
        DATABASE_NAME
    ).build()   //데이터베이스 빌더

    private val dao = database.CoordinateDAO()  //DAO
    private val executor = Executors.newSingleThreadExecutor()  //쓰레드

    fun getCoordinate(siDo: Int): LiveData<List<Coordinate>> =
        dao.getCoordinates(siDo)    //데이터 가져오기

    fun insertCoordinate(coordinate: Coordinate) =  //데이터 삽입
        executor.execute {
            dao.insertCoordinate(coordinate)
        }

    fun insertTestCase() =  //테스트케이스 삽입
        executor.execute {
            dao.insertCoordinate(coordinate = Coordinate(100, 35.2413866, 128.6958127))
        }


    companion object {  //싱글톤을 위한 동반객체
        private var INSTANCE: CoordinateRepository? = null

        fun initialize(context: Context) {  //최초 인스턴스 생성
            if (INSTANCE == null) {
                INSTANCE = CoordinateRepository(context)
            }
        }

        fun get(): CoordinateRepository {   //가져오기
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}