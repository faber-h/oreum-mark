package com.okre.oreummark.model

import com.google.gson.annotations.SerializedName

data class OreumData(
    val resultCode: String,
    val resultSummary: List<ResultSummary>,
    @SerializedName("reulstMsg")
    val resultMsg: String
)

data class ResultSummary(
    @SerializedName("explan")
    val explain: String,
    val imgPath: String,
    @SerializedName("oleumAddr")
    val oreumAddr: String,
    @SerializedName("oleumAltitu")
    val oreumAltitu: Int,
    @SerializedName("oleumEname")
    val oreumEname: String,
    @SerializedName("oleumKname")
    val oreumKname: String,
    val x: String,
    val y: String
)