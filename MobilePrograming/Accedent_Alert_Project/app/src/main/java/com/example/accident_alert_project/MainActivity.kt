package com.example.accident_alert_project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.accident_alert_project.data.Coordinate
import com.example.accident_alert_project.data.Location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var locationAPI: LocationAPI //데이터 파싱

    private var coordinateList: MutableList<Coordinate> = emptyList<Coordinate>().toMutableList()   //좌표쌍의 리스트
    private var coordinate: Coordinate = Coordinate(0.0, 0.0)

    private val retrofit: Retrofit = Retrofit.Builder() //retrofit 을 이용한 데이터파싱
        .baseUrl("http://apis.data.go.kr/")
        .addConverterFactory(GsonConverterFactory.create()) //변환기 - okhttp3.ResponseBody 객체를 String 으로
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationAPI = retrofit.create(LocationAPI::class.java)  //retrofit 을 이용한 데이터파싱

        getLocation()
    }

    fun getLocation() {
        val locationRequest: Call<Location> = locationAPI.getLocation()
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
                loc = response.body()!!
                Log.d("!!!!!!!!!!!!!!!!!!!",response.body().toString())
                Log.d("???좌표", response.body()!!.items.item[0].laCrd)

                for (i in response.body()!!.items.item.indices) {
                    coordinate.latitude = response.body()!!.items.item[i].laCrd.toDouble()
                    coordinate.longitude = response.body()!!.items.item[i].loCrd.toDouble()
                    coordinateList.add(coordinate)
                }
                Log.d("???좌표", coordinateList.toString())
            }
        })
    }
}