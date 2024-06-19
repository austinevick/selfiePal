package com.example.selfiepal.ui.authentication

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.selfiepal.ui.BottomNavigationActivity
import com.example.selfiepal.ui.LandingActivity
import com.example.selfiepal.viewmodel.AuthViewModel

class AuthStateChangeScreen():Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: AuthViewModel = hiltViewModel<AuthViewModel>()
        val state = viewModel.authState.collectAsState(initial = "")
        Log.d("userId", state.value)

        if (state.value.trim().isNotEmpty()){
          navigator?.push(BottomNavigationActivity())
        }else{
          navigator?.push(LandingActivity())
        }


    }
}