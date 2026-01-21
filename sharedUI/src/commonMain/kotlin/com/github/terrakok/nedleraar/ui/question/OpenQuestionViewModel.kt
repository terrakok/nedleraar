package com.github.terrakok.nedleraar.ui.question

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.nedleraar.DataService
import com.github.terrakok.nedleraar.Lesson
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Immutable
sealed interface Feedback {
    val answer: String
    val title: String
    val text: String
    data class Loading(
        override val answer: String,
        override val title: String = "",
        override val text: String = ""
    ) : Feedback
    data class Correct(
        override val answer: String,
        override val title: String,
        override val text: String
    ) : Feedback
    data class Incorrect(
        override val answer: String,
        override val title: String,
        override val text: String
    ) : Feedback
}

@AssistedInject
class OpenQuestionViewModel(
    @Assisted val id: String,
    val dataService: DataService
) : ViewModel() {
    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(id: String): OpenQuestionViewModel
    }

    var lesson by mutableStateOf<Lesson?>(null)
        private set

    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    private val answers = mutableStateMapOf<String, MutableState<String>>()
    private val results = mutableStateMapOf<String, Feedback>()

    val question get() = lesson?.let { it.questions[currentQuestionIndex] } ?: error("Lesson is null")
    val answer get() = answers.getOrPut(question.id) { mutableStateOf("") }
    val feedback get() = results[question.id]

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                lesson = dataService.getLesson(id)
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun getFeedback() {
        checkAnswer(question.id, answer.value)
    }

    private fun checkAnswer(qId: String, answer: String) {
        val lessonId = lesson?.id ?: return
        viewModelScope.launch {
            if (results[qId] is Feedback.Loading) return@launch
            results[qId] = Feedback.Loading(answer)
            val result = dataService.checkAnswer(lessonId, qId, answer)

            if (!result.contains("Score:")) {
                results[qId] = Feedback.Incorrect(answer, "Try again", result)
            } else {
                val regex = Regex("""Score:\s*([0-9]+(?:\.[0-9]+)?)\s*/\s*([0-9]+(?:\.[0-9]+)?)""")
                val match = regex.find(result)
                val score = match?.destructured?.component1()?.toFloatOrNull() ?: 0f

                if (score >= 3f) {
                    results[qId] = Feedback.Correct(answer, "Good", result)
                } else {
                    results[qId] = Feedback.Incorrect(answer, "Too many mistakes", result)
                }
            }
        }
    }

    fun nextQuestion() {
        currentQuestionIndex++
        val max = lesson?.questions?.size ?: 0
        currentQuestionIndex.coerceIn(0..<max)
    }

    fun previousQuestion() {
        currentQuestionIndex--
        val max = lesson?.questions?.size ?: 0
        currentQuestionIndex.coerceIn(0..<max)
    }
}