package com.example.selfiepal.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoadingProgress(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    CircularProgressIndicator(
        strokeWidth = 2.5.dp,
        color = color,
        modifier = modifier.size(18.dp),
    )
}