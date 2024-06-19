package com.example.selfiepal.model

import com.google.gson.annotations.SerializedName


data class FollowUserModel(
    @SerializedName("userId") val userId: String
)

data class UserProfileModel(
    @SerializedName("data")
    val `data`: UserProfileData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)

data class UserProfileData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("followers")
    val followers: List<String>,
    @SerializedName("following")
    val following: List<String>,
    @SerializedName("_id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("posts")
    val posts: List<Any>,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("__v")
    val v: Int
)