package com.example.instagramapp.model


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)