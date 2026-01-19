package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun YouTubeWidget(
    videoId: String,
    modifier: Modifier = Modifier
)