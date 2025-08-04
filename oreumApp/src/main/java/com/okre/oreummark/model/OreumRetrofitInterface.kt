package com.okre.oreummark.model

import retrofit2.http.GET

interface OreumRetrofitInterface {
    @GET("getOleumADetailList")
    suspend fun getOreumList() : OreumData

    companion object {
        var oreumService: OreumRetrofitInterface? = null
        fun getInstance() : OreumRetrofitInterface {
            if (oreumService == null) {
                val retrofit = RetrofitOkHttpManager.retrofitBuilder.build()
                oreumService = retrofit.create(OreumRetrofitInterface::class.java)
            }
            return oreumService!!
        }
    }
}