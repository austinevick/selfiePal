package com.example.selfiepal.ui.authentication.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.selfiepal.components.CustomTextField
import com.example.selfiepal.components.CustomTopBar
import com.example.selfiepal.theme.PrimaryColor

class SignUpStep2 : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val password = remember { mutableStateOf("") }
        val confirmPassword = remember { mutableStateOf("") }
        val isConfirmPasswordError = remember { mutableStateOf(false) }
        val isPasswordError = remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }



        Scaffold(
            topBar = { CustomTopBar(title = "Setup your password")},
            snackbarHost = { SnackbarHost(snackbarHostState) })
        { innerPadding ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,

                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                val modifier = Modifier.padding(horizontal = 16.dp)


                Spacer(modifier = Modifier.height(30.dp))


                CustomTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        isPasswordError.value = it.isEmpty()
                    },
                    placeholder = "Password",
                    isError = isPasswordError.value,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = modifier
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = confirmPassword.value,
                    onValueChange = {
                        confirmPassword.value = it
                        isConfirmPasswordError.value = it.isEmpty()
                    },
                    placeholder = "Confirm Password",
                    isError = isConfirmPasswordError.value,
                    modifier = modifier
                )
                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {

//                        if (password.value.isEmpty()) {
//                            isPasswordError.value = true
//                            return@Button
//                        }
//                        if (confirmPassword.value.isEmpty()) {
//                            isConfirmPasswordError.value = true
//                            return@Button
//                        }

                        navigator?.push(SignUpStep3())
                    },

                    modifier = modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) { Text(text = "Continue") }





            }
        }


    }
}