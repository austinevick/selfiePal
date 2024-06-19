package com.example.selfiepal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.selfiepal.theme.InstagramAppTheme
import com.example.selfiepal.ui.LandingActivity
import com.example.selfiepal.ui.authentication.AuthStateChangeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstagramAppTheme {
                Navigator(AuthStateChangeScreen()) {
                    SlideTransition(it)
                }
            }
        }
    }
}

