package com.github.terrakok.oefoef

import com.github.terrakok.oefoef.spellcheck.ClientSpellcheck
import com.russhwolf.settings.Settings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@OptIn(FlowPreview::class)
@SingleIn(AppScope::class)
@Inject
class FeedbackService(
    private val dataService: DataService,
    private val settings: Settings,
    private val json: Json,
    private val clientSpellcheck: ClientSpellcheck,
    appCoroutineScope: CoroutineScope
) {
    companion object {
        private const val FEEDBACK_STATE_KEY = "com.github.terrakok.oefoef.saved_feedback_key"
    }

    private val feedbackSynchronizedObject = SynchronizedObject()
    private val feedbackData = mutableMapOf<String, Feedback>().also { initState ->
        val saved = settings.getStringOrNull(FEEDBACK_STATE_KEY)?.let { str ->
            json.decodeFromString<Map<String, Feedback>>(str)
        }
        saved?.let { initState.putAll(it) }
    }

    private val feedbackDataFlow: MutableSharedFlow<Map<String, Feedback>> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val spellCheckRequestFlow = MutableSharedFlow<SpellCheckRequest>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        appCoroutineScope.launch {
            val saved = settings.getStringOrNull(FEEDBACK_STATE_KEY)
            if (saved != null) {
                Log.d("Restoring feedback state")
                feedbackData.putAll(json.decodeFromString<Map<String, Feedback>>(saved))
                feedbackDataFlow.emit(feedbackData)
            }
            feedbackDataFlow.debounce(1000)
                .collect {
                    Log.d("Saving feedback state")
                    settings.putString(FEEDBACK_STATE_KEY, json.encodeToString(it))
                }
        }

        if (clientSpellcheck.enabled) {
            appCoroutineScope.launch {
                spellCheckRequestFlow.debounce(500).collect { request ->
                    val words = request.answer.extractWords()
                    val incorrectWords = words.filter { !clientSpellcheck.correct(it) }
                    val prev = getFeedback(request.lessonId, request.questionId)
                    saveFeedback(
                        request.lessonId,
                        request.questionId,
                        prev.copy(spellcheck = SpellcheckResult(incorrectWords))
                    )
                }
            }
        }
    }

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

        if (clientSpellcheck.enabled) {
            spellCheckRequestFlow.tryEmit(SpellCheckRequest(lessonId, questionId, answer))
        }
    }

    suspend fun checkAnswer(
        lessonId: String,
        questionId: String
    ) {
        val prev = getFeedback(lessonId, questionId)
        if (prev.answer.isBlank()) {
            saveFeedback(lessonId, questionId, null)
            return
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

private data class SpellCheckRequest(
    val lessonId: String,
    val questionId: String,
    val answer: String
)

private val wordRegex = Regex("""\p{L}+""")
private fun String.extractWords(): List<String> =
    wordRegex.findAll(this).map { it.value }.toList()
