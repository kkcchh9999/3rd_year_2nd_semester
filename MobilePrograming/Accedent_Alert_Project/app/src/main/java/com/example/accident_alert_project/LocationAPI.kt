package com.example.accident_alert_project

import com.example.accident_alert_project.data.Location
import retrofit2.Call
import retrofit2.http.GET

interface LocationAPI {
    @GET("B552061/jaywalking/getRestJaywalking?" +
            "serviceKey=W8ehC9X8gh1OEHqMZh92XTL%2BOOkcr9Q3mErTlyrS%2Bh9qRF0CP9SGn6c4DdrJMaWPkKz5Ln1oT4RhyAT6A1EZcA%3D%3D" +
            "&searchYearCd=2017" +
            "&siDo=11" +
            "&guGun=320" +
            "&type=json" +
            "&numOfRows=100" +
            "&pageNo=1"
    )
    fun getLocation(): Call<Location>
}