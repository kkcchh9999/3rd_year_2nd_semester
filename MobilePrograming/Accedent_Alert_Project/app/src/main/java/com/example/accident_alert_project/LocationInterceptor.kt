package com.example.accident_alert_project

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LocationInterceptor : Interceptor{    //인터셉터, 새로 url 에 내용을 덧붙여줌.
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        Log.d("이거 체크요망", "${originalRequest.url} //ㅇㅇ")

        val newURL: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("type", "json")
            .addQueryParameter("numOfRows", "100")
            .addQueryParameter("pageNo", "1")
            .build()

        val newRequest: Request = originalRequest.newBuilder()
            .url(newURL)
            .build()

        Log.d("이거는?", "$newRequest //ㅇㅇ")

       return chain.proceed(newRequest)
    }
}