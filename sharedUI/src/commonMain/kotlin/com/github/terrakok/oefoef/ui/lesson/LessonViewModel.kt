package com.github.terrakok.oefoef.ui.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.oefoef.DataService
import com.github.terrakok.oefoef.Lesson
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.launch

@AssistedInject
class LessonViewModel(
    @Assisted val id: String,
    val dataService: DataService
) : ViewModel() {
    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(id: String): LessonViewModel
    }

    var lesson by mutableStateOf<Lesson?>(null)
        private set

    private val _transcriptItems = mutableStateListOf<TranscriptItem>()

    val transcriptItems: List<TranscriptItem>
        get() = _transcriptItems

    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                lesson = dataService.getLesson(id)
                lesson?.videoTranscription?.let { transcript ->
                    _transcriptItems.clear()
                    _transcriptItems.addAll(transcript.map { TranscriptItem(it.time, it.text) })
                }
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun translateTranscriptItem(index: Int) {
        _transcriptItems[index] = _transcriptItems[index].copy(translationInProgress = true)
        viewModelScope.launch {
            val enTranslation = dataService.translateTranscriptItem(index, id)
            _transcriptItems[index] = _transcriptItems[index].copy(
                translationInProgress = false,
                enTranslation = "EN: $enTranslation"
            )
        }.invokeOnCompletion { throwable ->
            if (throwable != null) {
                _transcriptItems[index] = _transcriptItems[index].copy(translationInProgress = true)
            }
        }
    }

    data class TranscriptItem(
        val time: Int,
        val text: String,
        val enTranslation: String = "",
        val translationInProgress: Boolean = false
    )
}