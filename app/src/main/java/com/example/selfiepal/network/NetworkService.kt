package com.example.selfiepal.network

import com.example.selfiepal.model.AuthResponseModel
import com.example.selfiepal.model.CommentModel
import com.example.selfiepal.model.CommentsResponseModel
import com.example.selfiepal.model.FollowUserModel
import com.example.selfiepal.model.LikePostModel
import com.example.selfiepal.model.LoginModel
import com.example.selfiepal.model.PostListModel
import com.example.selfiepal.model.PostResponseModel
import com.example.selfiepal.model.UserProfileModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface NetworkService {

    // AUTHENTICATION
    @POST("user/login")
    suspend fun login(@Body model: LoginModel): AuthResponseModel

    @POST("user/register")
    suspend fun register(@Body requestBody: RequestBody): Response<AuthResponseModel>

    @GET("user/register/{username}")
    suspend fun checkIfUsernameExists(@Path("username") username: String): Response<AuthResponseModel>

    // POST
    @POST("post/create")
    suspend fun createPost(
        @Body requestBody: RequestBody
    ):Response<PostResponseModel>

    @GET("post/all")
    suspend fun getAllPost(): Response<PostListModel>

    @PATCH("post/like/{postId}")
    suspend fun handleLike(
        @Body userId: LikePostModel,
        @Path("postId") postId: String
    ): Response<PostResponseModel>

    @PATCH("post/unLike/{postId}")
    suspend fun handleUnLike(
        @Body userId: LikePostModel,
        @Path("postId") postId: String
    ): Response<PostResponseModel>


    // COMMENTS
    @POST("comment/create")
    suspend fun createComment(@Body comment: CommentModel): Response<PostResponseModel>

    @GET("comment/all/{id}")
    suspend fun getCommentByPostId(@Path("id") postId: String): Response<CommentsResponseModel>

    @DELETE("comment/delete/{id}")
    suspend fun deleteComment(@Path("id") commentId: String): Response<PostResponseModel>

    // PROFILE
    @GET("user/profile/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): Response<UserProfileModel>

    @POST("user/follow")
    suspend fun followUser(@Body userId: FollowUserModel): Response<Any>

    @POST("user/unfollow")
    suspend fun unfollowUser(@Body userId: FollowUserModel): Response<Any>

}






