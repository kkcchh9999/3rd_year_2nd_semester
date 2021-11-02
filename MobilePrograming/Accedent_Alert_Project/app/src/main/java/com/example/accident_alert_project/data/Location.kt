package com.example.accident_alert_project.data


data class Location(
    var items: Items,
    var numOfRows: Int,
    var pageNo: Int,
    var resultCode: String,
    var resultMsg: String,
    var totalCount: Int
)