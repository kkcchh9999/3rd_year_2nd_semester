package com.example.finalproject

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import com.example.finalproject.data.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class MyService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient   //위치좌표 가져오기
    private val viewModel = CoordinateViewModel()
    private lateinit var coordinateList: LiveData<List<Coordinate>>
    private var myPosition: Coordinate = Coordinate(0,0.0, 0.0)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pref = applicationContext.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        val siDo = pref.getInt("siDo", 0)
        Log.d("시도코드", "$siDo")
        if (siDo != 0) {
            coordinateList = viewModel.getCoordinate(siDo)
        } else {
            coordinateList = viewModel.getCoordinate(siDo)
            Toast.makeText(applicationContext, "지역을 선택해 주세요", Toast.LENGTH_SHORT).show()
            onDestroy()
        }
        if (intent == null) {
            return START_STICKY
        } else {
            processCommand(intent)
        }
        return START_STICKY
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
                myPosition.longitude = lng
                myPosition.latitude = lat
                Toast.makeText(applicationContext, "$lat,$lng", Toast.LENGTH_SHORT).show()

                coordinateList.observeForever { //데이터베이스 Livedata 읽기
                    for (i in it.indices) {
                        Log.d("가능?", "lat 빼기: ${it[i].latitude - 0.0003},lat ${myPosition.latitude},lat 더하기: ${it[i].latitude + 0.0003}")
                        Log.d("가능?", "lng 빼기: ${it[i].longitude - 0.0003},lng ${myPosition.longitude},lat 더하기: ${it[i].longitude + 0.0003}")
                        if (it[i].longitude - 0.0003 <= myPosition.longitude
                            && myPosition.longitude <= it[i].longitude + 0.0003
                            && it[i].latitude - 0.0003 <= myPosition.latitude
                            && myPosition.latitude <= it[i].latitude + 0.0003
                        ) {
                            Toast.makeText(applicationContext, "일단 인근 좌표다", Toast.LENGTH_SHORT).show()

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val name = "name"
                                val descriptionText = "descriptionText"
                                val importance = NotificationManager.IMPORTANCE_HIGH
                                val mChannel = NotificationChannel("id", name, importance)
                                mChannel.description = descriptionText
                                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.createNotificationChannel(mChannel)
                            }

                            val builder = NotificationCompat.Builder(applicationContext, "id")
                                .setSmallIcon(R.drawable.ic_warning)
                                .setContentTitle("경고: 전방주시!!")
                                .setContentText("인근에 무단횡단 사고다발지역")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            with(NotificationManagerCompat.from(applicationContext)) {
                                notify(1, builder.build())
                            }
                        }

                    }
                }
            }
        } catch (e: Exception) {

        }


    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}