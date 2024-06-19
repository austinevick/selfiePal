package com.example.selfiepal.model

import com.google.gson.annotations.SerializedName

data class CommentModel(
    @SerializedName("comment") val comment: String,
    @SerializedName("postId") val postId: String,
    @SerializedName("createdby") val createdby: String,
)

data class CommentsResponseModel(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<CommentsResponseData> = arrayListOf()
)

data class CommentsResponseData(

    @SerializedName("_id") var id: String,
    @SerializedName("comment") var comment: String,
    @SerializedName("postId") var postId: String,
    @SerializedName("likes") var likes: ArrayList<String> = arrayListOf(),
    @SerializedName("createdby") var createdby: Createdby,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("__v") var _v: Int? = null,
    val showPopup:Boolean = false

)
