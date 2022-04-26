package com.example.android_networking.network.retrofit

import com.example.android_networking.network.retrofit.service.PosterService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHttp {
    val IS_TESTER = false
    val SERVER_DEVELOPMENT = "https://jsonplaceholder.typicode.com/"
    val SERVER_PRODUCTION = "http://10.0.2.2:8080/"
//    val SERVER_PRODUCTION = "https://6221f0e6666291106a17fe42.mockapi.io/"

    val retrofit = Retrofit.Builder()
        .baseUrl(server())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun server(): String{
        if (IS_TESTER) return SERVER_DEVELOPMENT
        return SERVER_PRODUCTION
    }

    val posterService: PosterService = retrofit.create(PosterService::class.java)

    fun <T> createService(service: Class<T>): T{
        return retrofit.create(service)
    }
}