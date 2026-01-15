package com.github.terrakok.nedleraar

import androidx.compose.runtime.Immutable

@Immutable
data class VideoData(
    val videoId: String,
    val title: String,
    val previewUrl: String,
    val lengthSeconds: Int,
    val text: List<VideoText>
)

data class VideoText(
    val timestamp: Long,
    val text: String
)