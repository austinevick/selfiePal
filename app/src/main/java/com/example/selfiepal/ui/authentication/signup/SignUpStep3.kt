package com.example.selfiepal.ui.authentication.signup

import android.content.Context
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.core.content.FileProvider
import androidx.core.os.BuildCompat
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.example.selfiePal.R
import com.example.selfiepal.components.CustomDialogLoader
import com.example.selfiepal.components.CustomOutlinedButton
import com.example.selfiepal.components.CustomTopBar
import com.example.selfiepal.model.RegisterModel
import com.example.selfiepal.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

data class SignUpStep3(
    val username: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val email: String,
    val password: String
) : Screen {
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

        val file = context.createImageFile()
        val uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            context.packageName + ".provider", file
        )
        val cameraLauncher = rememberLauncherForActivityResult(contract =
        ActivityResultContracts.TakePicture(),
            onResult = { isSuccess ->
                if (isSuccess) selectedImages.value = uri
            })

        val cameraPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) cameraLauncher.launch(uri)
        }

        val scope = rememberCoroutineScope()
        val isNotEmpty = selectedImages.value?.path?.isNotEmpty() == true
        fun dismissBottomSheet() {
            scope.launch {
                delay(2000)
                showBottomSheet.value = false
            }
        }

        Scaffold(
            topBar = {
                CustomTopBar(title = "Set up profile picture")
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(),
                    onClick = { showBottomSheet.value = true }) {
                    Text(text = "Choose a photo")
                }
                Spacer(modifier = Modifier.height(8.dp))
                CustomOutlinedButton(
                    text =
                    if (isNotEmpty) "Continue" else "Skip"
                ) {
                    if (isNotEmpty) {
                        val model = selectedImages.value?.let { it1 ->
                            RegisterModel(
                                uri = it1,
                                email = email,
                                username = username,
                                firstName = firstName,
                                lastName = lastName,
                                password = password,
                                gender = gender,
                                location = "Lagos"
                            )
                        }
                        scope.launch {
                            if (model != null) {
                                viewModel.register(context, navigator, model)
                            }
                        }
                    } else {
                        Toast.makeText(
                            context, "Please select a photo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                if (showBottomSheet.value) ModalBottomSheet(
                    containerColor = Color.White,
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                    onDismissRequest = { showBottomSheet.value = false }) {

                    CustomOutlinedButton(
                        text = "Take Photo",
                        Modifier.padding(horizontal = 16.dp)
                    ) {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        dismissBottomSheet()
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    CustomOutlinedButton(
                        text = "Add from Gallery",
                        Modifier.padding(horizontal = 16.dp)
                    ) {
                        photoLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts
                                    .PickVisualMedia.ImageOnly
                            )
                        )
                        dismissBottomSheet()
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        if (isLoading.value) CustomDialogLoader()

    }

    private fun Context.createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            externalCacheDir /* directory */
        )
    }
}