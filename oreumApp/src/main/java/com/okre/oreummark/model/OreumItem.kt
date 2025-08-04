package com.okre.oreummark.model

data class OreumItem (
    val idx: Int,
    val explain: String,
    val imgPath: String,
    val oreumAddr: String,
    val oreumName: String,
    val oreumLongitude: Double,
    val oreumLatitude: Double
)