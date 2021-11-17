package com.example.finalproject;

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.Internet.LocationAPI
import com.example.finalproject.Internet.LocationInterceptor
import com.example.finalproject.data.Coordinate
import com.example.finalproject.data.Location
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btn10: Button
    private lateinit var btn11: Button
    private lateinit var btn12: Button
    private lateinit var btn13: Button
    private lateinit var btn14: Button
    private lateinit var btn15: Button
    private lateinit var btn16: Button
    private lateinit var btn17: Button
    private lateinit var btn18: Button


    private lateinit var serviceIntent: Intent
    //공유 프리퍼런스
    private lateinit var locationAPI: LocationAPI //데이터 파싱
    private val client = OkHttpClient.Builder() //인터셉터 인스턴스 생성
        .addInterceptor(LocationInterceptor())
        .build()
    private val gson: Gson = GsonBuilder().setLenient().create()
    private val retrofit: Retrofit = Retrofit.Builder()     //retrofit 을 이용한 데이터파싱, 인스턴스 생성
        .baseUrl("http://apis.data.go.kr/")         //기본 Url
        .addConverterFactory(GsonConverterFactory.create(gson)) //변환기 - okhttp3.ResponseBody 객체를 String 으로
        .client(client)                                     //인터셉터 추가 -> 기본 URL 편집
        .build()

    private val viewModel = CoordinateViewModel()   //뷰모델

    private var locCodeArr = arrayOf(   //지역 코드 배열
        arrayOf(11, 680, 740, 305, 500, 620, 215, 530, 545, 350, 320,
            230, 590, 440, 410, 650, 200, 290, 710, 470, 560, 170,
            380, 110, 140, 260),   //서울
        arrayOf(26, 440, 410, 710, 290, 170, 260, 230, 320, 530, 380,
            140, 500, 470, 200, 110, 350), //부산
        arrayOf(27, 200, 290, 710, 140, 230, 170, 260, 110), //대구
        arrayOf(28, 710, 245, 170, 200, 140, 237, 260, 185, 720, 110),  //인천
        arrayOf(29, 200, 155, 110, 170, 140),   //광주
        arrayOf(30, 230, 110, 170, 200, 140),   //대전
        arrayOf(31, 140, 170, 200, 710, 110 ),   //울산
        arrayOf(41, 820, 281, 283, 285, 287, 290, 210, 610, 310, 410,
            570, 360, 250, 197, 199, 195, 135, 131, 133, 113, 117,
            111, 115, 390, 270, 273, 271, 550, 173, 171, 630, 830,
            730, 670, 800, 370, 460, 463, 465, 461, 430, 150, 500,
            480, 220, 810, 650, 450, 590),   //경기
        arrayOf(42, 150, 820, 170, 230, 210, 800, 830, 750, 130, 810,
            770, 780, 110, 190, 760, 720, 790, 730),    //강원
        arrayOf(43, 760, 800, 720, 740, 730, 770, 150, 745, 750, 710,
            111, 112, 114, 113, 130),   //충북
        arrayOf(44, 250, 150, 710, 230, 830, 270, 180, 760, 210, 770,
            200, 730, 810, 130, 131, 133, 790, 825, 800),   //충남
        arrayOf(45, 790, 130, 210, 190, 730, 800, 770, 710, 140, 750,
            740, 113, 111, 180, 720),   //전북
        arrayOf(46, 810, 770, 720, 230, 730, 170, 710, 110, 840, 780,
            150, 910, 130, 870, 830, 890, 880, 800, 900, 860, 820,
            790),                       //전남
        arrayOf(47, 290, 130, 830, 190, 720, 150, 280, 920, 250, 840,
            170, 770, 760, 210, 230, 900, 940, 930, 730, 820, 750,
            850, 111, 113),             //경북
        arrayOf(48, 310, 880, 820, 250, 840, 160, 270, 240, 860, 332,
            330, 720, 170, 190, 740, 110, 125, 127, 123, 121, 129,
            220, 850, 730, 870, 890),   //경남
        arrayOf(50, 130, 110)   //제주
    )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val locationPermissionRequest = registerForActivityResult(  //퍼미션 요청 생성
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        // Precise location access granted.
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                    } else -> {
                    Toast.makeText(applicationContext, "권한이 필요합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                    onDestroy() //토스트 생성, 앱 종료
                }
            }
        }

        locationPermissionRequest.launch(arrayOf(   //퍼미션 요청
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        )

        locationAPI = retrofit.create(LocationAPI::class.java)  //인스턴스 생성

        btnStart = findViewById(R.id.btn_start) //버튼 id 찾기
        btnStop = findViewById(R.id.btn_stop)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btn7 = findViewById(R.id.btn7)
        btn8 = findViewById(R.id.btn8)
        btn9 = findViewById(R.id.btn9)
        btn10 = findViewById(R.id.btn10)
        btn11 = findViewById(R.id.btn11)
        btn12 = findViewById(R.id.btn12)
        btn13 = findViewById(R.id.btn13)
        btn14 = findViewById(R.id.btn14)
        btn15 = findViewById(R.id.btn15)
        btn16 = findViewById(R.id.btn16)
        btn17 = findViewById(R.id.btn17)
        btn18 = findViewById(R.id.btn18)

        //공유 프리퍼런스
        val pref: SharedPreferences = getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit() //에디터

        //공유 프리퍼런스에서 최초 실행 확인
        val first: Boolean = pref.getBoolean("isFirst", true);
        if (first) {    //최초 실행시
            editor.putBoolean("isFirst", false) //false 로 저장
            editor.apply()

            //dialogue 보여주기
            val dialogBuilder = AlertDialog.Builder(this)
                .setTitle("설명서")
                .setMessage("보행자 사고다발지역에서 자동으로 알림을 발생시켜주는 어플리케이션입니다. 위치 권한이 필요합니다.\n \n" +
                        "현재 활동중인 지역을 선택한 후 알림 켜기를 눌러주세요. 이후에는 어플리케이션을 종료해도 무방합니다.")
                .setPositiveButton("확인") { _, _ -> }    //아무 동작 없는 버튼

            val dialog = dialogBuilder.create()
            //dialogue 디자인 설정
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_shape)
            //보여주기
            dialog.show()

            //데이터 다운로드, 시 도 코드 , 구 군 코드
            for (i in locCodeArr.indices) {
                for (j in 1 until locCodeArr[i].size) {
                    //다운로드 메소드 실행
                    download(locCodeArr[i][0], locCodeArr[i][j])
                }
            }
            viewModel.insertTestCase()  //테스트케이스 추가
        }

        serviceIntent = Intent(applicationContext, MyService::class.java)
        btnStart.setOnClickListener {
            startService(serviceIntent)//서비스 시작
        }

        btnStop.setOnClickListener {
            stopService(serviceIntent)     //서비스 종료
        }

        btn1.setOnClickListener {
            editor.putInt("siDo", 11).apply()   // 서울, 공유 프리퍼런스에 데이터 저장
            editor.apply()              //적용
            stopService(serviceIntent)  //진행중인 서비스 중단
            startService(serviceIntent) //새 서비스 시작  -> 바뀐 위치정보 사용
        }

        btn2.setOnClickListener {
            editor.putInt("siDo", 26)   //부산
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn3.setOnClickListener {
            editor.putInt("siDo", 27)   //대구
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn4.setOnClickListener {
            editor.putInt("siDo", 28)   //인천
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn5.setOnClickListener {
            editor.putInt("siDo", 29)   //광주
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn6.setOnClickListener {
            editor.putInt("siDo", 30)   //대전
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn7.setOnClickListener {
            editor.putInt("siDo", 31)   //울산
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn8.setOnClickListener {
            editor.putInt("siDo", 36)   // 세종
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn9.setOnClickListener {
            editor.putInt("siDo", 41)   //경기
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn10.setOnClickListener {
            editor.putInt("siDo", 42)   //강원
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn11.setOnClickListener {
            editor.putInt("siDo", 43)   //충북
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn12.setOnClickListener {
            editor.putInt("siDo", 44)   //충남
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn13.setOnClickListener {
            editor.putInt("siDo", 45)   //전북
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn14.setOnClickListener {
            editor.putInt("siDo", 46)   //전남
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn15.setOnClickListener {
            editor.putInt("siDo", 47)   //경북
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn16.setOnClickListener {
            editor.putInt("siDo", 48)   //경남
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn17.setOnClickListener {
            editor.putInt("siDo", 50)   //제주
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }

        btn18.setOnClickListener {
            editor.putInt("siDo", 100)
            editor.apply()
            stopService(serviceIntent)
            startService(serviceIntent)
        }
    }

    private fun download(SiDo: Int, guGun: Int) {
        //locationAPI 를 통한 request 생성
        val locationRequest: Call<Location> = locationAPI.getLocation(SiDo.toString(), guGun.toString())
        var loc: Location? = null   //Location 객체
        locationRequest.enqueue(object : Callback<Location> {   //쓰레드 실행
            override fun onFailure( //실패
                call: Call<Location>,
                t: Throwable
            ) {

            }

            override fun onResponse(    //응답
                call: Call<Location>,
                response: Response<Location>
            ) {
                if (response.body() == null) {  //null 검사
                    return
                }
                loc = response.body()

                for (i in response.body()!!.items.item.indices) {
                    val coordinate = Coordinate(SiDo, 0.0, 0.0) //시도코드, 위도, 경도로 이루어진 클래스
                    coordinate.latitude = response.body()!!.items.item[i].laCrd.toDouble()  //위도 입력
                    coordinate.longitude = response.body()!!.items.item[i].loCrd.toDouble() //경도 입력
                    viewModel.insertCoordinate(coordinate)  //뷰모델에 추가 -> DAO 추가 -> 데이터베이스에 저장
                }
            }
        })
    }
}