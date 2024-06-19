package com.example.selfiepal.model

import android.net.Uri


data class RegisterModel(
    val uri: Uri = Uri.EMPTY,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val gender: String,
    val location: String,
    val password: String,
)