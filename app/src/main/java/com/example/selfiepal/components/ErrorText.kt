package com.example.selfiepal.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
 fun ErrorText(message: String,
               modifier: Modifier = Modifier,
               color: Color= Color.Red) {
    Text(
        text = message,
        color = color, fontSize = 10.sp,
        style = TextStyle(platformStyle =
        PlatformTextStyle(includeFontPadding = false)
        ),modifier = modifier

    )
}
