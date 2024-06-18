package com.example.instagramapp.utilities


sealed class UIState {
    data object Loading : UIState()
    data class Error(val message: String) : UIState()
    data class Success(val data: Any) : UIState()
}

data class ButtonState(
    val loading: Boolean = false,
    val message: String = "")