package com.example.instagramapp.ui.authentication.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.instagramapp.R
import com.example.instagramapp.components.CircularLoadingProgress
import com.example.instagramapp.components.CustomTextField
import com.example.instagramapp.components.CustomTopBar
import com.example.instagramapp.components.ErrorText
import com.example.instagramapp.model.LoginModel
import com.example.instagramapp.theme.PrimaryColor
import com.example.instagramapp.ui.authentication.SignInActivity
import com.example.instagramapp.utilities.isValidUsername
import com.example.instagramapp.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpStep1 : Screen {
    @Composable
    override fun Content() {
        val viewmodel = hiltViewModel<AuthViewModel>()
        val isLoading = viewmodel.isLoading.collectAsState()
        val responseMessage = viewmodel.responseMessage.collectAsState("")
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.current

        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val lastname = remember { mutableStateOf("") }
        val firstname = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val gender = remember { mutableStateListOf("Male", "Female", "I prefer not to say") }
        val selectedGender = remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val isUsernameError = remember { mutableStateOf(false) }
        val isPasswordError = remember { mutableStateOf(false) }
        val isEmailError = remember { mutableStateOf(false) }
        val isFirstnameError = remember { mutableStateOf(false) }
        val isLastnameError = remember { mutableStateOf(false) }

        val usernameErrorMessage = remember { mutableStateOf("") }
        val firstnameErrorMessage = remember { mutableStateOf("") }
        val lastnameErrorMessage = remember { mutableStateOf("") }
        val emailErrorMessage = remember { mutableStateOf("") }


        Scaffold(topBar = {
            CustomTopBar(title = "Enter your personal info")
        },
            snackbarHost = { SnackbarHost(snackbarHostState) })
        { innerPadding ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
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
                    value = username.value,
                    onValueChange = {
                        username.value = it
                        isUsernameError.value = it.isEmpty()
                        scope.launch {
                            delay(1000)
                            viewmodel.checkIfUsernameExists(it)
                        }
                        if(!isValidUsername(it)){
                            usernameErrorMessage.value = "Username must be alphanumeric"
                        }else
                            usernameErrorMessage.value = ""
                    },
                    placeholder = "Username",
                    isError = isUsernameError.value,
                    modifier = modifier,
                    trailingIcon = {
                        if (isLoading.value)
                            CircularLoadingProgress(color = PrimaryColor)
                    }
                )
                ErrorText(message = responseMessage.value.ifEmpty
                { usernameErrorMessage.value },
                    color = if (usernameErrorMessage.value
                        .contentEquals("available")) Color.Green else Color.Red,
                    modifier = errorModifier)
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = firstname.value,
                    onValueChange = {
                        firstname.value = it
                        isFirstnameError.value = it.isEmpty()
                    },
                    placeholder = "First name",
                    isError = isFirstnameError.value,
                    modifier = modifier
                )
                ErrorText(message = firstnameErrorMessage.value,
                    modifier = errorModifier)
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = lastname.value,
                    onValueChange = {
                        lastname.value = it
                        isLastnameError.value = it.isEmpty()
                    },
                    placeholder = "Last name",
                    isError = isEmailError.value,
                    modifier = modifier
                )
                ErrorText(message = lastnameErrorMessage.value,
                    modifier = errorModifier)
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        isEmailError.value = it.isEmpty()
                    },
                    placeholder = "Email",
                    isError = isEmailError.value,
                    modifier = modifier
                )
                ErrorText(message = emailErrorMessage.value,
                    modifier = errorModifier)

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        if (username.value.isEmpty()&&firstname.value.isEmpty()&&
                            lastname.value.isEmpty()&&email.value.isEmpty()) {
                            usernameErrorMessage.value = "Username must be alphanumeric"
                            firstnameErrorMessage.value = "First name is required"
                            lastnameErrorMessage.value = "Last name is required"
                            emailErrorMessage.value = "Email is required"
                            isUsernameError.value = true
                            isFirstnameError.value = true
                            isLastnameError.value = true
                            isEmailError.value = true
                            return@Button
                        }

                        if (username.value.isEmpty()) {
                            isUsernameError.value = true
                            usernameErrorMessage.value = "Username is required"
                            return@Button
                        }
                        if (email.value.isEmpty()) {
                            isEmailError.value = true
                            emailErrorMessage.value = "Email is required"
                            return@Button
                        }
                        if (firstname.value.isEmpty()) {
                            isFirstnameError.value = true
                            firstnameErrorMessage.value = "First name is required"
                            return@Button
                        }
                        if (lastname.value.isEmpty()) {
                            isLastnameError.value = true
                            lastnameErrorMessage.value = "Last name is required"
                            return@Button
                        }

                        navigator?.push(SignUpStep2())
                    },
                    enabled = !isLoading.value,
                    modifier = modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    shape = RoundedCornerShape(10.dp)
                ) { Text(text = "Continue") }
                Spacer(
                    modifier = Modifier
                        .height(100.dp)
                        .weight(1f)
                )

                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(text = "Already have an account?")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Sign in", color = PrimaryColor,
                        modifier = Modifier.clickable
                        { navigator?.push(SignInActivity()) })
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }


    }
}