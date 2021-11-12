package com.example.finalproject

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
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
    private lateinit var thread: ServiceThread

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel("foreground", name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification = NotificationCompat.Builder(applicationContext, "foreground")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("알림 서비스 동작중...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(2, notification)

        val pref = applicationContext.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        val siDo = pref.getInt("siDo", 0)
        if (siDo != 0) {
            coordinateList = viewModel.getCoordinate(siDo)
        } else {
            coordinateList = viewModel.getCoordinate(siDo)
            Toast.makeText(applicationContext, "지역을 선택해 주세요!!", Toast.LENGTH_SHORT).show()
        }

        val handler = MyServiceHandler(intent)
        thread = ServiceThread(handler)
        thread.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        thread.stopNow()
        super.onDestroy()
    }
    @SuppressLint("MissingPermission")
    private fun processCommand() {
        val token = CancellationTokenSource()   //캔슬 토큰??
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) //위치 가져오기
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,     //높은 정확성
                token.token //토큰
            ).addOnSuccessListener {
                val lng = it.longitude
                val lat = it.latitude
                myPosition.longitude = lng
                myPosition.latitude = lat

                coordinateList.observeForever { livedata -> //데이터베이스 Livedata 읽기
                    for (i in livedata.indices) {
                        if (livedata[i].longitude - 0.0003 <= myPosition.longitude
                            && myPosition.longitude <= livedata[i].longitude + 0.0003
                            && livedata[i].latitude - 0.0003 <= myPosition.latitude
                            && myPosition.latitude <= livedata[i].latitude + 0.0003
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val name = "name"
                                val descriptionText = "descriptionText"
                                val importance = NotificationManager.IMPORTANCE_HIGH
                                val mChannel = NotificationChannel("id", name, importance)
                                mChannel.description = descriptionText
                                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                                notificationManager.createNotificationChannel(mChannel)
                            }

                            val intent = Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

                            val builder = NotificationCompat.Builder(applicationContext, "id")
                                .setSmallIcon(R.drawable.ic_warning)
                                .setContentTitle("경고: 전방주시!!")
                                .setContentText("인근에 무단횡단 사고다발지역")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
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


    inner class ServiceThread(private val handler: Handler) : Thread() {
        private var isRun = true

        override fun run() {
            while (isRun) {
                handler.sendEmptyMessage(0);
                try {
                    sleep(20000)
                } catch (e: Exception) {

                }
            }
        }

        fun stopNow() {
            synchronized(this) {
                this.isRun = false
            }
        }
    }

    inner class MyServiceHandler(private val intent: Intent?) : Handler() {
        override fun handleMessage(msg: Message) {
            processCommand()
        }
    }
}