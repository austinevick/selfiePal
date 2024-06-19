package com.example.selfiepal.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(modifier: Modifier = Modifier, uri: Uri) {
    val context = LocalContext.current
    val player = ExoPlayer.Builder(context).build()
        .apply {
            prepare()
            setMediaItem(MediaItem.fromUri(uri))
            playWhenReady = true
        }

    val lifecycle = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current


    Box {
        
        AndroidView(
            modifier = modifier.fillMaxWidth().height(200.dp),
            factory = {

                PlayerView(it).also { playerView ->
                    playerView.player = player
                    playerView.useController = false
                }
            },
            update = {
                when (lifecycle.value) {
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    Lifecycle.Event.ON_STOP -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    else -> Unit
                }
            }

        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle.value = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        val listener =
            object : Player.Listener {
            }

        player.addListener(listener)

        onDispose {
            player.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

