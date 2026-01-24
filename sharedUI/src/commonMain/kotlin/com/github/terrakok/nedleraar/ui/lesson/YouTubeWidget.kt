package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

@Immutable
class YouTubeController {
    var play by mutableStateOf(false)
    val progress = Channel<Int>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val seek = Channel<Int>(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun onProgress(progress: Int) {
        this.progress.trySend(progress)
    }

    fun seekTo(seek: Int) {
        play = true
        this.seek.trySend(seek)
        this.progress.trySend(seek)
    }
}

@Composable
expect fun YouTubeWidget(
    videoId: String,
    controller: YouTubeController,
    modifier: Modifier = Modifier
)