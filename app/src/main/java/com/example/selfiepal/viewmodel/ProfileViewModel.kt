package com.example.selfiepal.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.AndroidViewModel
import com.example.selfiepal.model.FollowUserModel
import com.example.selfiepal.network.NetworkService
import com.example.selfiepal.utilities.ButtonState
import com.example.selfiepal.utilities.UIState
import com.example.selfiepal.utilities.hasInternetConnection
import com.example.selfiepal.utilities.noInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val networkService: NetworkService
) : AndroidViewModel(application) {

    private val _userProfileState = MutableStateFlow<UIState>(UIState.Loading)
    val userProfileState: StateFlow<UIState> = _userProfileState.asStateFlow()
    private val _buttonState = MutableStateFlow(ButtonState(loading = false))
    val buttonState: StateFlow<ButtonState> = _buttonState.asStateFlow()

    private val _following = MutableStateFlow(mutableListOf<String>())
    val following: StateFlow<MutableList<String>> = _following.asStateFlow()


    suspend fun followUser(userId: String, snackbarState: SnackbarHostState) {
        try {
            val response = networkService.followUser(FollowUserModel(userId))
            Log.d("follow", response.code().toString())
            Log.d("follow", response.body().toString())
        } catch (e: Exception) {
            Log.d("follow", e.toString())
        }
    }
    suspend fun unfollowUser(userId: String, snackbarState: SnackbarHostState) {
        try {
            val response = networkService.unfollowUser(FollowUserModel(userId))
            Log.d("unfollow", response.code().toString())
        } catch (e: Exception) {
            Log.d("unfollow", e.message.toString())
        }
    }

    suspend fun getUserProfile(userId: String) {
        if (hasInternetConnection(getApplication())) {
            try {
                val response = networkService.getUserProfile(userId)
                _userProfileState.value = UIState.Success(data = response.body()!!.data)
            } catch (e: Exception) {
                Log.d("error", e.message.toString())
                _userProfileState.value = UIState.Error(e.message.toString())
            }
        } else {
            _userProfileState.value = UIState.Error(noInternetConnection)
        }
    }

}