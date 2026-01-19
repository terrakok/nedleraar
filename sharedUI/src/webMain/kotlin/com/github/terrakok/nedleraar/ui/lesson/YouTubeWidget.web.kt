package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import org.w3c.dom.HTMLIFrameElement

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun YouTubeWidget(
    videoId: String,
    modifier: Modifier
) {
    val videoUrl = "https://www.youtube.com/embed/${videoId}?autoplay=1"
    Box(modifier) {
        WebElementView(
            factory = {
                (document.createElement("iframe") as HTMLIFrameElement).apply {
                    src = videoUrl
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}