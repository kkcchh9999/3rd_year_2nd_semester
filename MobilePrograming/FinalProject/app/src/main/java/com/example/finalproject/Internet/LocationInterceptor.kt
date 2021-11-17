package com.example.finalproject.Internet;

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LocationInterceptor : Interceptor{    //인터셉터, 새로 url 에 내용을 덧붙여줌.
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val newURL: HttpUrl = originalRequest.url.newBuilder()  //기존의 URLd에 파라미터 추가
            .addQueryParameter("type", "json")
            .addQueryParameter("numOfRows", "100")
            .addQueryParameter("pageNo", "1")
            .build()

        val newRequest: Request = originalRequest.newBuilder()  //request 요청
            .url(newURL)
            .build()

       return chain.proceed(newRequest)
    }
}