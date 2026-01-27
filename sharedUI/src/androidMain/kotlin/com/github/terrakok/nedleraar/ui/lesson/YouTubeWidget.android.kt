package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.channels.getOrElse

@Composable
actual fun YouTubeWidget(
    videoId: String,
    controller: YouTubeController,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        var player: YouTubePlayer? by remember { mutableStateOf(null) }

        LaunchedEffect(player) {
            player?.let { p ->
                p.loadVideo(videoId, controller.seek.tryReceive().getOrElse { 0 }.toFloat())
                for (position in controller.seek) {
                    p.seekTo(position.toFloat())
                }
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                YouTubePlayerView(context).also { view ->
                    lifecycle.addObserver(view)
                    view.enableAutomaticInitialization = false
                    view.initialize(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                player = youTubePlayer
                            }

                            override fun onCurrentSecond(
                                youTubePlayer: YouTubePlayer,
                                second: Float
                            ) {
                                controller.onProgress(second.toInt())
                            }
                        }
                    )
                }
            },
            onRelease = { view ->
                view.release()
            }
        )
    }
}