package com.example.selfiepal.viewmodel

import android.app.Application
import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import cafe.adriel.voyager.navigator.Navigator
import com.example.selfiePal.R
import com.example.selfiepal.model.AuthResponseModel
import com.example.selfiepal.model.LoginModel
import com.example.selfiepal.model.RegisterModel
import com.example.selfiepal.network.NetworkService
import com.example.selfiepal.preferences.Preferences
import com.example.selfiepal.ui.BottomNavigationActivity
import com.example.selfiepal.utilities.getFileFromUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val networkService: NetworkService,
    private val preferences: Preferences
) : AndroidViewModel(application) {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _responseMessage = MutableLiveData<String?>(null)
    val responseMessage: Flow<String?> = _responseMessage.asFlow()

    private val _authState = MutableLiveData("")
    val authState: Flow<String> = _authState.asFlow()


    init {
        preferences.getUserId().let { _authState.value = it }
    }

    override fun onCleared() {
        super.onCleared()
        _responseMessage.value = null
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun login(
        model: LoginModel,
        navigator: Navigator?,
        snackbarState: SnackbarHostState
    ) {
        try {
            _isLoading.emit(true)
            val response = networkService.login(model)
            when (response.status) {
                200 -> {
                    preferences.saveToken(response.token)
                    preferences.saveUserId(response.data.id)
                    navigator?.push(BottomNavigationActivity())
                }

                400 -> {
                    _isLoading.emit(false)
                    Log.d("res", response.message)
                    snackbarState.showSnackbar("Invalid credentials")
                }

                401 -> {
                    _isLoading.emit(false)
                    Log.d("res", response.message)
                    snackbarState.showSnackbar("Invalid credentials")
                }
            }

        } catch (e: HttpException) {
            _isLoading.emit(false)
            snackbarState.showSnackbar(R.string.something_went_wrong.toString())
            Log.d("error", e.message.toString())
        }
    }


    suspend fun register(context: Context, navigator: Navigator?, model: RegisterModel) {
        try {
            _isLoading.emit(true)
            val file = getFileFromUri(context, model.uri)
            val requestBody = "imageUrl".toRequestBody(MultipartBody.FORM)
            val fileRequestBody = file?.asRequestBody(MultipartBody.FORM)
            val multipartBodyPath =
                MultipartBody.Part.createFormData("imageUrl", file?.name, fileRequestBody!!)
            val multipartBody = MultipartBody.Builder()
                .addPart(multipartBodyPath)
                .setType(MultipartBody.FORM)
                .addPart(requestBody)
                .addFormDataPart("firstName", model.firstName)
                .addFormDataPart("lastName", model.lastName)
                .addFormDataPart("password", model.password)
                .addFormDataPart("username", model.username)
                .addFormDataPart("email", model.email)
                .addFormDataPart("gender", model.gender)
                .addFormDataPart("location", model.location)
                .build()
            val response = networkService.register(multipartBody)
            val data = response.body()
            if (response.code() == 201) {
                preferences.saveToken(data!!.token)
                preferences.saveUserId(data.data.id)
                navigator?.push(BottomNavigationActivity())
            }
            Log.d("register", model.toString())
            Log.d("res", response.toString())
            _isLoading.emit(false)
        } catch (e: Exception) {
            _isLoading.emit(false)
            Log.d("error", e.message.toString())
        }
    }

    suspend fun checkIfUsernameExists(username: String) {
        try {
            _isLoading.emit(true)
            val response = networkService.checkIfUsernameExists(username)
            Log.d("register", response.toString())
            Log.d("res", response.body().toString())
            _isLoading.emit(false)

            response.body().let {
                _responseMessage.value = response.body()?.message
            }
        } catch (e: Exception) {
            _isLoading.emit(false)
            _responseMessage.value = e.message.toString()
            Log.d("error", e.message.toString())
        }
    }


    fun logout() {
        preferences.deleteToken()
    }
}