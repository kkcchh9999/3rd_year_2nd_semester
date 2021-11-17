package com.example.finalproject.data;

import com.example.finalproject.data.Items


data class Location(
    //공공데이터 포털에서 제공하는 API 사용시
    //가져오는 JSON 파일의 형식
    var items: Items,   //아이템 객체
    var numOfRows: Int, //열의 수
    var pageNo: Int,    //페이지번호
    var resultCode: String, //결과코드
    var resultMsg: String,  //결과 메시지
    var totalCount: Int     //총 데이터 수
)