package com.github.terrakok.nedleraar

import androidx.compose.runtime.Immutable

@Immutable
data class VideoData(
    val id: String,
    val title: String,
    val previewUrl: String,
    val lengthSeconds: Int,
    val text: List<VideoText>
)

data class VideoText(
    val timestamp: Long,
    val text: String
)

@Immutable
data class Lesson(
    val id: String,
    val title: String,
    val videoData: VideoData,
    val questions: List<OpenQuestions>
)

data class OpenQuestions(
    val id: String,
    val text: String
)