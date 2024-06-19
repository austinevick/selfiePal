package com.example.selfiepal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialogLoader() {
    BasicAlertDialog(modifier = Modifier
    .fillMaxSize()
    .background(Color.Gray.copy(alpha = 0.2f)),
    properties = DialogProperties(usePlatformDefaultWidth = false,
    decorFitsSystemWindows = false),
    onDismissRequest = {  }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ){
            CircularLoadingProgress(Modifier.size(50.dp))
            Text(text = "Please wait...", color = Color.White)
        }
    }
}