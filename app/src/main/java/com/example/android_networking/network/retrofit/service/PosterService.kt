package com.example.android_networking.network.retrofit.service

import com.example.android_networking.model.Poster
import com.example.android_networking.model.PosterResp
import retrofit2.Call
import retrofit2.http.*

//@JvmSuppressWildcards
interface PosterService {

    @Headers(
        "Content-type:application/json"
    )

    @GET("posts")
    fun listPost(): Call<ArrayList<PosterResp>>

    @GET("posts/{id}")
    fun singlePost(@Path("id") id: Int): Call<PosterResp>

    @POST("posts")
    fun createPost(@Body post: PosterResp): Call<PosterResp>

    @PUT("posts/{id}")
    fun updatePost(@Path("id") id: Int, @Body post: PosterResp): Call<PosterResp>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<PosterResp>
}