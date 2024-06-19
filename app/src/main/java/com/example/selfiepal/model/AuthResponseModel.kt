package com.example.selfiepal.model


import com.google.gson.annotations.SerializedName

data class AuthResponseModel(
    @SerializedName("data")
    val `data`: AuthResponseData,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val status: Int,
    @SerializedName("token")
    val token: String
)


data class AuthResponseData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("__v")
    val v: Int
)