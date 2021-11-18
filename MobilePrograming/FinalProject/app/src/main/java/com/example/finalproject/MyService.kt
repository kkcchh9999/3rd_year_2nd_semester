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
            val name = "포그라운드 알림 채널"
            val importance = NotificationManager.IMPORTANCE_LOW //중요도 낮음
            val mChannel = NotificationChannel("foreground", name, importance)  //채널 선언
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)     //채널 생성
        }
        //알림 클릭시 때 동작을 위함
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        //알림 선언
        val notification = NotificationCompat.Builder(applicationContext, "foreground")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("알림 서비스 동작중...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(2, notification)    //포그라운드 서비스 실행,

        //공유 프리퍼런스, 앱 재시작시 지역 선택을 다시 하지 않기 위해
        val pref = applicationContext.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        val siDo = pref.getInt("siDo", 0)
        if (siDo != 0) {    //값이 있으면
            coordinateList = viewModel.getCoordinate(siDo)  //시도코드로 데이터베이스에서 값 읽기
        } else {    //값이 없으면
            coordinateList = viewModel.getCoordinate(siDo)
            Toast.makeText(applicationContext, "지역을 선택해 주세요!!", Toast.LENGTH_SHORT).show()
            stopService(intent) //서비스 종료
        }

        //서비스 핸들러
        val handler = MyServiceHandler()
        //쓰레드
        thread = ServiceThread(handler)
        //쓰레드 시작
        thread.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    //서비스가 종료될 때
    override fun onDestroy() {
        //쓰레드 중지
        thread.stopNow()
        super.onDestroy()
    }
    @SuppressLint("MissingPermission")
    private fun processCommand() {
        val token = CancellationTokenSource()   //위치를 가져오는데 사용하는 토큰
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) //위치 가져오기
        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,     //높은 정확성
            token.token //토큰
        ).addOnSuccessListener {    //위치 가져오기 성공시
            myPosition.longitude = it.longitude
            myPosition.latitude = it.latitude

            coordinateList.observeForever { livedata -> //데이터베이스 Livedata 읽기
                for (i in livedata.indices) {   //좌표 탐색
                    if (livedata[i].longitude - 0.0003 <= myPosition.longitude
                        && myPosition.longitude <= livedata[i].longitude + 0.0003
                        && livedata[i].latitude - 0.0003 <= myPosition.latitude
                        && myPosition.latitude <= livedata[i].latitude + 0.0003
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   //알림 채널 생성
                            val name = "name"
                            val importance = NotificationManager.IMPORTANCE_HIGH
                            val mChannel = NotificationChannel("id", name, importance)
                            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                            notificationManager.createNotificationChannel(mChannel)
                        }
                        //알림 클릭시 동작
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

                        //알림 빌더 생성
                        val builder = NotificationCompat.Builder(applicationContext, "id")
                            .setSmallIcon(R.drawable.ic_warning)
                            .setContentTitle("경고: 전방주시!!")
                            .setContentText("인근에 무단횡단 사고다발지역")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                        //알림 발생
                        with(NotificationManagerCompat.from(applicationContext)) {
                            notify(1, builder.build())
                        }
                    }

                }
            }
        }
    }

    //쓰레드
    inner class ServiceThread(private val handler: Handler) : Thread() {
        private var isRun = true

        override fun run() {
            while (isRun) { //무한루프
                handler.sendEmptyMessage(0);    //핸들러의 명령 처리, 전송 데이터값 0
                try {
                    sleep(10000)    //10초 대기
                } catch (e: Exception) {

                }
            }
        }

        fun stopNow() { //isRun false 로 루프 중지
            isRun = false
        }
    }

    @SuppressLint("HandlerLeak")
    inner class MyServiceHandler : Handler() {
        override fun handleMessage(msg: Message) {  //핸들러의 명령
            processCommand()    //processCommand 처리
        }
    }
}