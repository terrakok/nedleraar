package com.github.terrakok.nedleraar

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

data class LessonHeader(
    val id: String,
    val title: String,
    val previewUrl: String,
    val lengthSeconds: Int,
    val createdAt: Instant,
)

@Immutable
data class Lesson(
    val id: String,
    val videoId: String,
    val title: String,
    val previewUrl: String,
    val lengthSeconds: Int,
    val videoTranscription: List<TranscriptionItem>,
    val questions: List<OpenQuestion>,
    val createdAt: Instant,
    val lang: String,
)

data class TranscriptionItem(
    val timestamp: Int,
    val text: String
)

data class OpenQuestion(
    val id: String,
    val text: String,
    val textEn: String
)