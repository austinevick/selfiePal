package com.example.instagramapp.model

import com.google.gson.annotations.SerializedName

data class PostResponseModel(
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Int,
    @SerializedName("data") val `data`: Any? =null,
)