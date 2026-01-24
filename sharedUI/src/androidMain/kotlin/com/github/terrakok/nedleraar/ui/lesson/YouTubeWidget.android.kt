package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

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
        Text(
            text = "YouTube video: $videoId",
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}