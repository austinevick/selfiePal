package com.example.selfiepal.ui.authentication.signup

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.selfiepal.components.CircularLoadingProgress
import com.example.selfiepal.components.CustomTextField
import com.example.selfiepal.components.CustomTopBar
import com.example.selfiepal.components.ErrorText
import com.example.selfiepal.theme.PrimaryColor
import com.example.selfiepal.ui.authentication.SignInActivity
import com.example.selfiepal.utilities.isValidUsername
import com.example.selfiepal.viewmodel.AuthViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpStep1 : Screen {
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewmodel = hiltViewModel<AuthViewModel>()
        val isLoading = viewmodel.isLoading.collectAsState()
        val responseMessage = viewmodel.responseMessage.collectAsState("")
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.current

        val locationPermission = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        val showDropDown = remember { mutableStateOf(false) }

        val username = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val lastname = remember { mutableStateOf("") }
        val firstname = remember { mutableStateOf("") }
        val location = remember { mutableStateOf("") }
        val gender = remember { mutableStateListOf("Male", "Female", "I prefer not to say") }
        val selectedGender = remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }

        val isUsernameError = remember { mutableStateOf(false) }
        val isGenderError = remember { mutableStateOf(false) }
        val isEmailError = remember { mutableStateOf(false) }
        val isFirstnameError = remember { mutableStateOf(false) }
        val isLastnameError = remember { mutableStateOf(false) }

        val usernameErrorMessage = remember { mutableStateOf("") }
        val firstnameErrorMessage = remember { mutableStateOf("") }
        val lastnameErrorMessage = remember { mutableStateOf("") }
        val emailErrorMessage = remember { mutableStateOf("") }
        val genderErrorMessage = remember { mutableStateOf("") }


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
                    .imePadding()
            ) {
                val modifier = Modifier.padding(horizontal = 16.dp)
                val errorModifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start)

                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it.trim()
                        isUsernameError.value = it.isEmpty()
                        scope.launch {
                            delay(2000)
                            viewmodel.checkIfUsernameExists(it)
                        }
                        if (!isValidUsername(it)) {
                            usernameErrorMessage.value = "Username must be alphanumeric"
                        } else
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
                ErrorText(
                    message = if (responseMessage.value != null)
                        "${responseMessage.value}" else
                        usernameErrorMessage.value,
                    color = Color.Red,
                    modifier = errorModifier
                )
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = firstname.value,
                    onValueChange = {
                        firstname.value = it.trim()
                        isFirstnameError.value = it.isEmpty()
                        if (firstname.value.isEmpty()) {
                            isFirstnameError.value = true
                            firstnameErrorMessage.value = "First name is required"
                        }else{
                            firstnameErrorMessage.value = ""
                        }
                    },
                    placeholder = "First name",
                    isError = isFirstnameError.value,
                    modifier = modifier
                )
                ErrorText(
                    message = firstnameErrorMessage.value,
                    modifier = errorModifier
                )
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = lastname.value,
                    onValueChange = {
                        lastname.value = it.trim()
                        isLastnameError.value = it.isEmpty()
                        if (lastname.value.isEmpty()) {
                            isLastnameError.value = true
                           lastnameErrorMessage.value = "Last name is required"
                        }else{
                            lastnameErrorMessage.value = ""
                        }
                    },
                    placeholder = "Last name",
                    isError = isEmailError.value,
                    modifier = modifier
                )
                ErrorText(
                    message = lastnameErrorMessage.value,
                    modifier = errorModifier
                )
                Spacer(modifier = Modifier.height(6.dp))
                CustomTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it.trim()
                        isEmailError.value = it.isEmpty()
                        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                       if (!isValidEmail){
                           isEmailError.value = true
                           emailErrorMessage.value = "Invalid email"
                       }else if (email.value.isEmpty()) {
                            isEmailError.value = true
                            emailErrorMessage.value = "Email is required"
                        }else {
                            emailErrorMessage.value = ""
                        }
                    },
                    placeholder = "Email",
                    isError = isEmailError.value,
                    modifier = modifier
                )
                ErrorText(
                    message = emailErrorMessage.value,
                    modifier = errorModifier
                )

                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = modifier
                    .fillMaxWidth()
                    .clickable { showDropDown.value = true }
                    .border(
                        1.dp, if (isGenderError.value) Color.Red else
                            Color.Gray.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedGender.value.isEmpty())
                                "Gender" else selectedGender.value,
                            color = if (selectedGender.value.isEmpty())
                                Color.Gray else Color.Black,
                            fontSize = 16.sp,
                            fontWeight = if (selectedGender.value.isEmpty())
                                FontWeight.Normal else FontWeight.W600
                        )
                        Icon(
                            Icons.Default.ArrowDropDown, tint = Color.Gray,
                            contentDescription = null
                        )
                    }
                    if (showDropDown.value) DropdownMenu(expanded = showDropDown.value,
                        modifier = Modifier.background(Color.White),
                        onDismissRequest = { showDropDown.value = false }) {
                        gender.forEach {
                            DropdownMenuItem(text = { Text(text = it) },
                                onClick = { selectedGender.value = it; showDropDown.value = false })
                        }
                    }
                }
                ErrorText(message = genderErrorMessage.value, errorModifier)

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        if (username.value.isEmpty() && firstname.value.isEmpty() &&
                            lastname.value.isEmpty() && email.value.isEmpty() &&
                            selectedGender.value.isEmpty()
                        ) {
                            usernameErrorMessage.value = "Username must be alphanumeric"
                            firstnameErrorMessage.value = "First name is required"
                            lastnameErrorMessage.value = "Last name is required"
                            emailErrorMessage.value = "Email is required"
                            genderErrorMessage.value = "Gender is required"
                            isUsernameError.value = true
                            isFirstnameError.value = true
                            isLastnameError.value = true
                            isEmailError.value = true
                            isGenderError.value = true
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
                        if (selectedGender.value.isEmpty()) {
                            isGenderError.value = true
                            genderErrorMessage.value = "Gender is required"
                            return@Button
                        }

                        navigator?.push(
                            SignUpStep2(
                                username.value, firstname.value,
                                lastname.value, selectedGender.value,
                                email.value
                            )
                        )
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