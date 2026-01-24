package com.github.terrakok.nedleraar

import dev.zacsweers.metro.Inject
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Inject
class FeedbackService(
    val dataService: DataService
) {
    private val feedbackSynchronizedObject = SynchronizedObject()
    private val feedbackData = mutableMapOf<String, Feedback>()

    private val feedbackDataFlow: MutableSharedFlow<Map<String, Feedback>> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private fun feedbackId(lessonId: String, questionId: String) = "$lessonId/$questionId"

    private fun saveFeedback(lessonId: String, questionId: String, feedback: Feedback?) {
        synchronized(feedbackSynchronizedObject) {
            val id = feedbackId(lessonId, questionId)
            if (feedback == null) {
                feedbackData.remove(id)
            } else {
                feedbackData[id] = feedback
            }
            feedbackDataFlow.tryEmit(feedbackData)
        }
    }

    private fun getFeedback(
        lessonId: String,
        questionId: String
    ): Feedback = synchronized(feedbackSynchronizedObject) {
        val id = feedbackId(lessonId, questionId)
        feedbackData[id] ?: EmptyFeedback()
    }

    fun getFeedbackState(
        lessonId: String,
        questionId: String
    ): Flow<Feedback> {
        val id = feedbackId(lessonId, questionId)
        return feedbackDataFlow.map { it[id] ?: EmptyFeedback() }
    }

    fun updateAnswer(
        lessonId: String,
        questionId: String,
        answer: String
    ) {
        val prev = getFeedback(lessonId, questionId)
        val newStatus = if (prev.status == FeedbackStatus.DRAFT) FeedbackStatus.DRAFT else FeedbackStatus.OUTDATED
        saveFeedback(lessonId, questionId, prev.copy(answer = answer, status = newStatus))
    }

    suspend fun checkAnswer(
        lessonId: String,
        questionId: String
    ) {
        val prev = getFeedback(lessonId, questionId)
        if (prev.answer.isBlank()) {
            saveFeedback(lessonId, questionId, null)
        }

        try {
            saveFeedback(lessonId, questionId, prev.copy(status = FeedbackStatus.LOADING))
            val feedbackStr = dataService.checkAnswer(lessonId, questionId, prev.answer)
            val feedback = Feedback(prev.answer, processFeedback(feedbackStr), FeedbackStatus.ACTUAL)
            saveFeedback(lessonId, questionId, feedback)
        } catch (e: Throwable) {
            saveFeedback(
                lessonId,
                questionId,
                Feedback(
                    prev.answer,
                    FeedbackResult("Error", "Network error: $e", false),
                    FeedbackStatus.ACTUAL
                )
            )
        }
    }

    private suspend fun processFeedback(text: String): FeedbackResult = withContext(Dispatchers.Default) {
        if (!text.contains("Score:")) {
            FeedbackResult("Try again", "Unknown error", false)
        } else {
            val regex = Regex("""Score:\s*([0-9]+(?:\.[0-9]+)?)\s*/\s*([0-9]+(?:\.[0-9]+)?)""")
            val match = regex.find(text)
            val score = match?.destructured?.component1()?.toFloatOrNull() ?: 0f
            FeedbackResult("Good", text, score >= 3f)
        }
    }
}