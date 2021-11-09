package com.example.finalproject

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.finalproject.data.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class MyService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient   //위치좌표 가져오기
    private val viewModel = CoordinateViewModel()
    private val coordinateList = viewModel.testJeju


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            return START_STICKY
        } else {
            processCommand(intent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun processCommand(intent: Intent?) {
        val token = CancellationTokenSource()
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) //위치 가져오기
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,     //높은 정확성
                token.token //토큰
            ).addOnSuccessListener {
                val lng = it.longitude
                val lat = it.latitude
                Log.d("제발!!!", "$lat,$lng")
                Toast.makeText(applicationContext, "$lat, $lng", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {

        }


        coordinateList.observeForever {
            for (i in it.indices) {
                Log.d("제주 체크", "${it[i].longitude}, ${it[i].longitude}")
            }
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}