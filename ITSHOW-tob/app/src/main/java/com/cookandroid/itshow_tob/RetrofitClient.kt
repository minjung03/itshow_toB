package com.cookandroid.itshow_tob

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit 객체를 생성하는 Calss
//BaseURL을 사용하여 실제 통신을 진행할 Retrofit Instance를 생성
object RetrofitClient {
    private val BASE_URL = "localhost:3000"

    val instance : WriteRecruitment by lazy{
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create(WriteRecruitment::class.java)
    }

}