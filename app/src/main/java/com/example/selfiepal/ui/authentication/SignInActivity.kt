package com.example.selfiepal.ui.authentication

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.selfiepal.components.CircularLoadingProgress
import com.example.selfiepal.components.CustomTextField
import com.example.selfiepal.components.CustomTopBar
import com.example.selfiepal.components.ErrorText
import com.example.selfiepal.model.LoginModel
import com.example.selfiepal.theme.PrimaryColor
import com.example.selfiepal.ui.authentication.signup.SignUpStep1
import com.example.selfiepal.utilities.isValidEmail
import com.example.selfiepal.utilities.isValidUsername
import com.example.selfiepal.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class SignInActivity : Screen {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Composable
    override fun Content() {
        val viewmodel = hiltViewModel<AuthViewModel>()
        val navigator = LocalNavigator.current
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val isLoading = viewmodel.isLoading.collectAsState()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val isUsernameError = remember { mutableStateOf(false) }
        val isPasswordError = remember { mutableStateOf(false) }
        val usernameErrorMessage = remember { mutableStateOf("") }
        val passwordErrorMessage = remember { mutableStateOf("") }

        Scaffold(
            topBar = { CustomTopBar(title = "Sign In")},
            snackbarHost = { SnackbarHost(snackbarHostState) })
        { innerPadding ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                val modifier = Modifier.padding(horizontal = 16.dp)

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it.trim()
                        isUsernameError.value = it.isEmpty()
                        if (!isValidEmail(username.value) && !isValidUsername(username.value)) {
                            usernameErrorMessage.value = "Invalid username or email"
                        } else {
                            usernameErrorMessage.value = ""
                        }
                    },
                    placeholder = "Username or email",
                    isError = isUsernameError.value,
                    modifier = modifier)
                ErrorText(message = usernameErrorMessage.value,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start)
                    )
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it.trim()
                        isPasswordError.value = it.isEmpty()
                        if (password.value.isEmpty()) {
                            passwordErrorMessage.value = "Password is required"
                        } else {
                            passwordErrorMessage.value = ""
                        }
                                    },
                    placeholder = "Password",
                    isError = isPasswordError.value,
                    modifier = modifier)
                ErrorText(message = passwordErrorMessage.value,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start))
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(34.dp)
                ) {
                    Text(text = "Forgot password?")
                }
                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        if(username.value.isEmpty() && password.value.isEmpty()){
                            isUsernameError.value = true
                            isPasswordError.value = true
                            usernameErrorMessage.value = "Username or email is required"
                            passwordErrorMessage.value = "Password is required"
                            return@Button
                        }
                        if(!isValidEmail(username.value) && !isValidUsername(username.value)){
                            isUsernameError.value = true
                            usernameErrorMessage.value = "Invalid username or email"
                            return@Button
                        }
                        if (username.value.isEmpty()) {
                            isUsernameError.value = true
                            usernameErrorMessage.value = "Username or email is required"
                            return@Button
                        }
                        if (password.value.isEmpty()) {
                            isPasswordError.value = true
                            passwordErrorMessage.value = "Password is required"
                            return@Button
                        }

                        scope.launch {
                            val model = LoginModel(username.value, password.value)
                            viewmodel.login(model, navigator, snackbarHostState)
                        }
                    },
                    enabled = !isLoading.value,
                    modifier = modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) {
                  if (isLoading.value) {
                 CircularLoadingProgress()
                  }else{
                      Text(text = "Log in")
                  }
                }
                Spacer(modifier = Modifier
                    .height(100.dp)
                    .weight(1f))

                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(text = "Don't have an account?")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Sign up", color = PrimaryColor,
                        modifier = Modifier.clickable {navigator?.push(SignUpStep1()) })
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }


    }
}