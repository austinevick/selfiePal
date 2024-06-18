package com.example.instagramapp.model


import com.google.gson.annotations.SerializedName

data class LikePostModel(
    @SerializedName("userId")
    val userId: String
)