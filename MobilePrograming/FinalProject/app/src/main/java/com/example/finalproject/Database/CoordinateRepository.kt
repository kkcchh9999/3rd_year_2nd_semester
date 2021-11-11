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
    ).build()

    private val dao = database.CoordinateDAO()
    private val executor = Executors.newSingleThreadExecutor()

    fun getCoordinate(siDo: Int): LiveData<List<Coordinate>> =
        dao.getCoordinates(siDo)

    fun insertCoordinate(coordinate: Coordinate) =
        executor.execute {
            dao.insertCoordinate(coordinate)
        }

    fun insertTestCase() =
        executor.execute {
            dao.insertTestCase(coordinate = Coordinate(100, 35.2413866, 128.6958127))
        }

    companion object {
        private var INSTANCE: CoordinateRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CoordinateRepository(context)
            }
        }

        fun get(): CoordinateRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}