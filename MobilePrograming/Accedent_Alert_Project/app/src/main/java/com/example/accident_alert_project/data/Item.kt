package com.example.accident_alert_project.data


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("afos_fid")
    var afosFid: Int,
    @SerializedName("afos_id")
    var afosId: String,
    @SerializedName("bjd_cd")
    var bjdCd: String,
    @SerializedName("caslt_cnt")
    var casltCnt: Int,
    @SerializedName("dth_dnv_cnt")
    var dthDnvCnt: Int,
    @SerializedName("geom_json")
    var geomJson: String,
    @SerializedName("la_crd")
    var laCrd: String,
    @SerializedName("lo_crd")
    var loCrd: String,
    @SerializedName("occrrnc_cnt")
    var occrrncCnt: Int,
    @SerializedName("se_dnv_cnt")
    var seDnvCnt: Int,
    @SerializedName("sido_sgg_nm")
    var sidoSggNm: String,
    @SerializedName("sl_dnv_cnt")
    var slDnvCnt: Int,
    @SerializedName("spot_cd")
    var spotCd: String,
    @SerializedName("spot_nm")
    var spotNm: String,
    @SerializedName("wnd_dnv_cnt")
    var wndDnvCnt: Int
)