package com.example.selfiepal.ui.authentication.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.example.selfiepal.components.CustomTopBar
import com.example.selfiepal.viewmodel.AuthViewModel
import com.selfiePal.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpStep3: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel = hiltViewModel<AuthViewModel>()
        val context = LocalContext.current
        val navigator = LocalNavigator.current

        val isLoading = viewModel.isLoading.collectAsState()
        val showBottomSheet = remember { mutableStateOf(false) }
        val selectedImages = remember { mutableStateOf<Uri?>(Uri.EMPTY) }
        val photoLauncher = rememberLauncherForActivityResult(contract =
        ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImages.value = uri })

        val cameraLauncher = rememberLauncherForActivityResult(contract =
        ActivityResultContracts.TakePicture(),
            onResult = { uri -> })
        val scope = rememberCoroutineScope()
        val isNotEmpty = selectedImages.value?.path?.isNotEmpty() == true


        Scaffold(
            topBar = {
                CustomTopBar(title ="Set up profile picture")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                if (isNotEmpty) AsyncImage(
                    model = selectedImages.value,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(180.dp)
                        .align(Alignment.CenterHorizontally)
                ) else Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = { showBottomSheet.value = true }) {
                    Text(text = "Choose a photo")
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
//                        val model = RegisterModel(
//                            uri = selectedImages.value!!,
//                            firstName = model.firstName,
//                            lastName = model.lastName,
//                            phone = model.phone,
//                            password = model.password
//                        )
//                        scope.launch {
//                            viewModel.register(context,navigator, model)
//                        }
                    }) {
                    Text(text = if (isNotEmpty) "Continue" else "Skip", color = Color.White)
                }

                if (showBottomSheet.value) ModalBottomSheet(
                    containerColor = Color.White,
                    onDismissRequest = { showBottomSheet.value = false }) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {

                        }) {
                        Text(text = "Take Photo", color = Color.Black)
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = {
                            photoLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                            scope.launch {
                                delay(2000)
                                showBottomSheet.value = false
                            }

                        }) {
                        Text(text = "Add from Gallery", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(60.dp))


                }
            }
        }



    }
}