package com.example.instagramapp.viewmodel

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.instagramapp.model.AuthResponseModel
import com.example.instagramapp.model.LoginModel
import com.example.instagramapp.model.RegisterModel
import com.example.instagramapp.network.NetworkService
import com.example.instagramapp.preferences.Preferences
import com.example.instagramapp.ui.BottomNavigationActivity
import com.example.instagramapp.utilities.getFileFromUri
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

    private val _responseMessage = MutableLiveData("")
    val responseMessage: Flow<String> = _responseMessage.asFlow()


    suspend fun login(
        model: LoginModel,
        navigator: Navigator?,
        snackbarState: SnackbarHostState
    ) {
        try {
            _isLoading.emit(true)
            val response = networkService.login(model)
            Log.d("register", model.toString())
            Log.d("res", response.toString())

            val data = response.body()
            if (response.code() == 200) {
                preferences.saveToken(data!!.token)
                preferences.saveUserId(data.data.id)
                navigator?.push(BottomNavigationActivity())
            } else
                _isLoading.emit(false)
            response.body()?.let { Log.d("res", it.message) }
            snackbarState.showSnackbar("Invalid credentials")
        } catch (e: Exception) {
            _isLoading.emit(false)
            snackbarState.showSnackbar(e.message.toString())
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
                .addFormDataPart("phone", model.phone)
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
            Log.d("res", response.body().toString())
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
