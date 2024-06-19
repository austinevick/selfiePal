package com.example.selfiepal.model


import android.net.Uri
import com.google.gson.annotations.SerializedName

data class PostModel(
    val uri: Uri,
    val caption: String,
    val mediaType: String
)


data class PostListModel(
    @SerializedName("data") val `data`: List<PostData>,
    @SerializedName("length") val length: Int,
    @SerializedName("limit") val limit: Any,
    @SerializedName("message") val message: String,
    @SerializedName("page") val page: Int,
    @SerializedName("status") val status: Int
)

data class PostData(
    @SerializedName("caption") val caption: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("creator") val creator: Creator,
    @SerializedName("_id") val id: String,
    @SerializedName("media") val media: String,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("likes") val likes: List<Any>,
    @SerializedName("comments") val comments: List<Comments>,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("__v") val v: Int
)

data class Creator(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("_id") val id: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("followers") val followers: List<Any>,
    @SerializedName("following") val following: List<Any>,
    @SerializedName("phone") val phone: String,
    @SerializedName("username") val username: String,

)

data class Comments(
    @SerializedName("_id") var id: String? = null,
    @SerializedName("comment") var comment: String,
    @SerializedName("postId") var postId: String? = null,
    @SerializedName("likes") var likes: ArrayList<String> = arrayListOf(),
    @SerializedName("createdby") var createdby: Createdby,
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null,
)
data class Createdby (

    @SerializedName("_id"      ) var Id       : String? = null,
    @SerializedName("username" ) var username : String? = null,
    @SerializedName("imageUrl" ) var imageUrl : String? = null

)

