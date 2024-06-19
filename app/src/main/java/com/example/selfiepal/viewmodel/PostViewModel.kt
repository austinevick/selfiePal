package com.example.selfiepal.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.selfiepal.model.LikePostModel
import com.example.selfiepal.model.PostModel
import com.example.selfiepal.network.NetworkService
import com.example.selfiepal.preferences.Preferences
import com.example.selfiepal.utilities.ButtonState
import com.example.selfiepal.utilities.UIState
import com.example.selfiepal.utilities.getFileFromUri
import com.example.selfiepal.utilities.hasInternetConnection
import com.example.selfiepal.utilities.noInternetConnection
import com.example.selfiepal.utilities.somethingwentwrong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val postService: NetworkService,
    private val preferences: Preferences,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) : AndroidViewModel(application) {

    private val _postListState = MutableStateFlow<UIState>(UIState.Loading)
    val postListState: StateFlow<UIState> = _postListState.asStateFlow()

    private val _uploadState = MutableStateFlow(ButtonState())
    val uploadState: StateFlow<ButtonState> = _uploadState.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    val isVisible = MutableStateFlow(false)


    init {
        viewModelScope.launch {
            getPosts()
            readUserId()
        }
    }

    @SuppressLint("MissingPermission")
    fun showUploadNotification() {
        notificationManager.notify(
            1, notificationBuilder
                .setContentTitle(uploadState.value.message)
                .setProgress(0, 0, true)
                .setOngoing(uploadState.value.loading)
                .build()
        )
    }

    suspend fun uploadPost(context: Context,snackbarState: SnackbarHostState, model: PostModel) {
        try {
            _uploadState.value = ButtonState(loading = true, message = "Uploading...")
            val file = getFileFromUri(context, model.uri)
            val requestBody = "media".toRequestBody(MultipartBody.FORM)
            val fileRequestBody = file?.asRequestBody(MultipartBody.FORM)
            val multipartBodyPath =
                MultipartBody.Part.createFormData("media", file?.name, fileRequestBody!!);

            val multipartBody = MultipartBody.Builder()
                .addPart(multipartBodyPath)
                .setType(MultipartBody.FORM)
                .addPart(requestBody)
                .addFormDataPart("caption", model.caption)
                .addFormDataPart("media_type", model.mediaType)
                .addFormDataPart("creator", userId.value)
                .build()
            val response = postService.createPost(multipartBody)
            Log.d("userId", _userId.value)
            Log.d("res", response.toString())
            if (response.isSuccessful) {
                _uploadState.value = ButtonState(loading = false, message = "Uploaded successfully")
                snackbarState.showSnackbar("Your post has been uploaded successfully")
            }
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            _uploadState.value = ButtonState(loading = false, message = e.message.toString())
        }
    }

    suspend fun getPosts() {
        if (hasInternetConnection(getApplication())) {
            try {
                val response = postService.getAllPost()
                _postListState.value = UIState.Success(data = response.body()!!.data)

            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                _postListState.value = UIState.Error(somethingwentwrong)
            }
        } else {
            _postListState.value = UIState.Error(noInternetConnection)
        }
    }

    suspend fun handleLikes(postId: String, userId: String) {
        try {
            val userIdModel = LikePostModel(userId)
            val response = postService.handleLike(userId = userIdModel, postId = postId)
            Log.d("[Logs]", response.code().toString())

        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
    }

    suspend fun handleUnLikes(postId: String, userId: String) {
        try {
            val response = postService.handleUnLike(userId = LikePostModel(userId), postId = postId)
            Log.d("[Logs]", response.code().toString())
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
    }

    private fun readUserId() {
        _userId.value = preferences.getUserId()
    }

}
