package com.example.android_networking.model

import com.google.gson.annotations.SerializedName

data class PosterResp(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName(value = "userId")
    val userId: Int = 0,
    @SerializedName(value = "title")
    val title: String? = null,
    @SerializedName(value = "body")
    val body: String? = null
) {
}