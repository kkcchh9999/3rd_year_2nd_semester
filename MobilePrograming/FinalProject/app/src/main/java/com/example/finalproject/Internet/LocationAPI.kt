package com.example.finalproject.Internet;

import com.example.finalproject.data.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationAPI {
    @GET("B552061/jaywalking/getRestJaywalking?" +
            "serviceKey=W8ehC9X8gh1OEHqMZh92XTL%2BOOkcr9Q3mErTlyrS%2Bh9qRF0CP9SGn6c4DdrJMaWPkKz5Ln1oT4RhyAT6A1EZcA%3D%3D" +
            "&searchYearCd=2019"
    )
    fun getLocation(
        @Query("SiDo") SiDo: String,
        @Query("guGun") guGun: String
    ): Call<Location>
}