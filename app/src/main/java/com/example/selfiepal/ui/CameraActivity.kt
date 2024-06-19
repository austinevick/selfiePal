package com.example.selfiepal.ui

import android.app.Activity
import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraActivity() {
    @OptIn(ExperimentalPermissionsApi::class)

        val cameraPermission = rememberMultiplePermissionsState(
            permissions =
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO
            ).toList()
        )
        val context = LocalContext.current
        val window = (LocalContext.current as? Activity)?.window
        val lensFacing = CameraSelector.LENS_FACING_BACK
        val lifecycleOwner = LocalLifecycleOwner.current
        val preview = Preview.Builder().build()
        val previewView = remember { PreviewView(context) }
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val imageCapture = remember { ImageCapture.Builder().build() }

        fun requestPermission() {
            if (cameraPermission.permissions.all
                { it.status != PermissionStatus.Granted }) {
                cameraPermission.launchMultiplePermissionRequest()
            }
        }

        LaunchedEffect(true) {
            requestPermission()

            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            preview.setSurfaceProvider(previewView.surfaceProvider)
        }


        Box(modifier = Modifier.fillMaxSize()

        ){

            AndroidView(
                { previewView },
                modifier = Modifier.fillMaxSize()
            )
        }


}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }