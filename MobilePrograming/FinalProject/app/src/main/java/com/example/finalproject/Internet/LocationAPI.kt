package com.example.finalproject.Internet;

import com.example.finalproject.data.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationAPI {
    @GET("B552061/jaywalking/getRestJaywalking?" +
            "serviceKey=W8ehC9X8gh1OEHqMZh92XTL%2BOOkcr9Q3mErTlyrS%2Bh9qRF0CP9SGn6c4DdrJMaWPkKz5Ln1oT4RhyAT6A1EZcA%3D%3D" +
            "&searchYearCd=2019"
    )   //기본 주소 + 주소 덧붙이기
    fun getLocation(
        @Query("SiDo") SiDo: String,    //&Sido=입력값
        @Query("guGun") guGun: String   //&guGun=입력값 을 주소에 덧붙임
    ): Call<Location>
}