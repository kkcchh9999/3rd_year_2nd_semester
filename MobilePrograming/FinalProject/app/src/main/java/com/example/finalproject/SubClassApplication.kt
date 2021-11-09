package com.example.finalproject

import android.app.Application
import com.example.finalproject.Database.CoordinateRepository

class SubClassApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CoordinateRepository.initialize(applicationContext)
    }
}