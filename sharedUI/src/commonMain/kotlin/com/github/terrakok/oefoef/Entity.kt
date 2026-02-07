package com.github.terrakok.oefoef

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
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
    val time: Int,
    val text: String
)

data class OpenQuestion(
    val id: String,
    val text: String,
    val textEn: String
)

enum class FeedbackStatus {
    DRAFT,
    LOADING,
    ACTUAL,
    OUTDATED
}

@Serializable
data class FeedbackResult(
    val title: String,
    val message: String,
    val isCorrect: Boolean
)

@Serializable
data class Feedback(
    val answer: String,
    val result: FeedbackResult?,
    val status: FeedbackStatus,
    val spellcheck: SpellcheckResult? = null
)

@Serializable
data class SpellcheckResult(
    val incorrectWords: List<String>
)

fun EmptyFeedback() = Feedback("", null, FeedbackStatus.DRAFT)
