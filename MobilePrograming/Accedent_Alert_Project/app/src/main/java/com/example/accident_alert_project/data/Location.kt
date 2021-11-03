package com.example.accident_alert_project.data


data class Location(
    //공공데이터 포털에서 제공하는 API 사용시
    //가져오는 JSON 파일의 형식
    var items: Items,
    var numOfRows: Int,
    var pageNo: Int,
    var resultCode: String,
    var resultMsg: String,
    var totalCount: Int
)