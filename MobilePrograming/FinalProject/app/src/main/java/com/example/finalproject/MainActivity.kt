package com.example.finalproject;

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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

const val REQUEST_PERMISSION_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var btnUpdate: ImageButton
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var tv: TextView

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
    //private var coordinateList: MutableList<Coordinate> = emptyList<Coordinate>().toMutableList()   //좌표쌍의 리스트
    private var locCodeArr = arrayOf(
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

        val locationPermissionRequest = registerForActivityResult(
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
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        )

        locationAPI = retrofit.create(LocationAPI::class.java)  //인스턴스 생성

        btnUpdate = findViewById(R.id.btn_update)
        btnStart = findViewById(R.id.btn_start)
        btnStop = findViewById(R.id.btn_stop)
        tv = findViewById(R.id.tv)

        btnUpdate.setOnClickListener {
            for (i in locCodeArr.indices) {
                for (j in 1 until locCodeArr[i].size) {
                    update(locCodeArr[i][0], locCodeArr[i][j])
                }
            }
        }

        btnStart.setOnClickListener {
            val intent = Intent(applicationContext, MyService::class.java)
            startService(intent)
        }

        btnStop.setOnClickListener {
            val intent = Intent(applicationContext, MyService::class.java)
            stopService(intent)
        }
    }

    private fun update(SiDo: Int, guGun: Int) {
        val locationRequest: Call<Location> = locationAPI.getLocation(SiDo.toString(), guGun.toString())
        var loc: Location? = null
        locationRequest.enqueue(object : Callback<Location> {
            override fun onFailure(
                call: Call<Location>,
                t: Throwable
            ) {
                Log.e("ERROR!!", "Failed to get Location", t)
            }

            override fun onResponse(
                call: Call<Location>,
                response: Response<Location>
            ) {
                if (response.body() == null) {
                    return
                }
                loc = response.body()

                for (i in response.body()!!.items.item.indices) {
                    val coordinate = Coordinate(SiDo, 0.0, 0.0)
                    coordinate.latitude = response.body()!!.items.item[i].laCrd.toDouble()
                    coordinate.longitude = response.body()!!.items.item[i].loCrd.toDouble()
                    Log.d("이거 왜이럼?", "$coordinate")
                    viewModel.insertCoordinate(coordinate)
                    //coordinateList.add(coordinate)
                }
            }
        })
    }
}