package com.example.selfiepal.ui

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import com.example.selfiePal.R

class HomeActivity(val modifier: Modifier) : Screen {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
       val navigator = LocalNavigator.current
        val pagerState = rememberPagerState(pageCount = { 3 })
        val expandText = remember { mutableStateOf(false) }
        val showDialog = remember { mutableStateOf(false) }
        val window = (LocalContext.current as? Activity)?.window




        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = { IconButton(
                        onClick = {
                            showDialog.value=true
                        }) {
                        Icon(painter = painterResource(id = R.drawable.camera),
                            modifier = Modifier.padding(6.dp),
                            contentDescription = null)
                    }},
                    title = { Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null, modifier = Modifier.width(100.dp)
                ) })
            }
        ) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(3) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        AsyncImage(
                            model = "https://cdn.pixabay.com/photo/2023/09/20/15/47/fish-8265114_1280.jpg",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Joshua_20",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Lagos, Nigeria", fontSize = 13.sp,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    AsyncImage(
                        model = "https://cdn.pixabay.com/photo/2022/11/17/12/45/leaves-7597975_1280.jpg",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth(),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.like),
                            modifier = Modifier.size(25.dp),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(14.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.comment),
                            modifier = Modifier.size(25.dp),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.messanger),
                            modifier = Modifier.size(25.dp),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "1,234 likes", fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val text: AnnotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Joshua_20" + " ") }
                        withStyle(style = SpanStyle(fontSize = 14.sp)) { append("Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia, molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium optio, eaque rerum! Provident similique accusantium nemo autem.") }
                    }
                    Text(text = text, maxLines = if (expandText.value) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { expandText.value = !expandText.value })
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }

           if (showDialog.value) Dialog(
                properties = DialogProperties(
                    usePlatformDefaultWidth =false ),
                onDismissRequest = { showDialog.value=false }) {
                CameraActivity()
            }
        }

    }
}
