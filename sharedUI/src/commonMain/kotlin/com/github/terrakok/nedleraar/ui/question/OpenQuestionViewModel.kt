package com.github.terrakok.nedleraar.ui.question

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.nedleraar.DataService
import com.github.terrakok.nedleraar.Feedback
import com.github.terrakok.nedleraar.FeedbackService
import com.github.terrakok.nedleraar.Lesson
import com.github.terrakok.nedleraar.OpenQuestion
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AssistedInject
class OpenQuestionViewModel(
    @Assisted val id: String,
    val dataService: DataService,
    val feedbackService: FeedbackService
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

    val question: OpenQuestion get() = lesson?.let { it.questions[currentQuestionIndex] } ?: error("Lesson is null")
    val feedback: Flow<Feedback> get() = feedbackService.getFeedbackState(id, question.id)

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

    fun updateAnswer(answer: String) {
        feedbackService.updateAnswer(lesson!!.id, question.id, answer)
    }

    fun checkAnswer() {
        viewModelScope.launch {
            feedbackService.checkAnswer(lesson!!.id, question.id)
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