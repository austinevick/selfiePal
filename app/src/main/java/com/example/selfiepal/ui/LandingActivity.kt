package com.example.selfiepal.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.selfiePal.R
import com.example.selfiepal.theme.PrimaryColor
import com.example.selfiepal.ui.authentication.SignInActivity
import com.example.selfiepal.ui.authentication.signup.SignUpStep1

class LandingActivity : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painterResource(id = R.drawable.background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(),
                    colorFilter = ColorFilter.tint(
                        Color.Black.copy(alpha = 0.4f),
                        BlendMode.Darken
                    ),
                    contentScale = ContentScale.FillHeight
                )

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(350.dp)
                )

                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                        .fillMaxWidth()
                ) {
                    val configuration = LocalConfiguration.current.screenWidthDp.dp/2.2f
                    Button(onClick = { navigator?.push(SignUpStep1()) },
                        modifier = Modifier.width(configuration),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        shape = RoundedCornerShape(10.dp),
                        content = {Text(text = "Sign Up")})
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = { navigator?.push(SignInActivity()) },
                        modifier = Modifier.width(configuration),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        shape = RoundedCornerShape(10.dp),
                        content = {Text(text = "Sign In")}
                    )
                }

            }


        }

    }

}