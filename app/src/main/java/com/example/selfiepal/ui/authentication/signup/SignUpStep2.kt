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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.selfiePal.R
import com.example.selfiepal.components.CustomTextField
import com.example.selfiepal.components.CustomTopBar
import com.example.selfiepal.components.ErrorText
import com.example.selfiepal.theme.PrimaryColor

data class SignUpStep2(
    val username:String,
    val firstName:String,
    val lastName:String,
    val gender:String,
    val email:String,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var isVisible by remember { mutableStateOf(false) }

        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        var isConfirmPasswordError by remember { mutableStateOf(false) }
        var isPasswordError by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }

        var passwordErrorMessage by remember { mutableStateOf("") }
        var confirmPassErrorMessage by remember { mutableStateOf("") }




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
                val errorModifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start)

                Spacer(modifier = Modifier.height(30.dp))


                CustomTextField(
                    value = password,
                    onValueChange = {
                        password = it.trim()
                        isPasswordError = it.isEmpty()
                        if (password.isEmpty()) {
                            isPasswordError = true
                            passwordErrorMessage = "Password is required"
                        } else {
                            passwordErrorMessage = ""
                        }
                    },
                    placeholder = "Password",
                    isError = isPasswordError,
                    visualTransformation =if (isVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = modifier,
                    trailingIcon = {
                        IconButton(onClick = { isVisible = !isVisible }) {
                            Icon(painterResource(id =
                            if (isVisible) R.drawable.visibility else
                                R.drawable.visibility_off),
                                contentDescription ="", tint = Color.Gray )
                        }
                    },
                )
                ErrorText(
                    message = passwordErrorMessage,
                    modifier = errorModifier)
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it.trim()
                        isConfirmPasswordError = it.isEmpty()
                        if (confirmPassword.trim() != password.trim()){
                            isConfirmPasswordError = true
                            confirmPassErrorMessage = "Passwords do not match"
                        }else{
                            confirmPassErrorMessage = ""
                        }
                    },
                    placeholder = "Confirm Password",
                    isError = isConfirmPasswordError,
                    modifier = modifier,
                    trailingIcon = {
                        IconButton(onClick = { isVisible = !isVisible }) {
                       Icon(painterResource(id =
                       if (isVisible) R.drawable.visibility else
                           R.drawable.visibility_off),
                           contentDescription ="" ,tint = Color.Gray )
                        }
                    },
                    visualTransformation =if (isVisible) VisualTransformation.None
                    else PasswordVisualTransformation()
                )
                ErrorText(
                    message = confirmPassErrorMessage,
                    modifier = errorModifier
                )
                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        if(password.isEmpty() && confirmPassword.isEmpty()){
                            isPasswordError = true
                            isConfirmPasswordError = true
                            passwordErrorMessage = "Password is required"
                            confirmPassErrorMessage = "Confirm password is required"
                            return@Button
                        }

                        if (password.isEmpty()) {
                            isPasswordError = true
                            return@Button
                        }
                        if (confirmPassword.isEmpty()) {
                            isConfirmPasswordError = true
                            return@Button
                        }
                        navigator?.push(SignUpStep3(
                            username = username,
                            firstName = firstName,
                            lastName = lastName,
                            gender = gender,
                            email = email,
                            password = password
                        ))
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