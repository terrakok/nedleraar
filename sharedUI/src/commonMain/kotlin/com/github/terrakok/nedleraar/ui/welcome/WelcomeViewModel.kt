package com.github.terrakok.nedleraar.ui.welcome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.nedleraar.DataService
import com.github.terrakok.nedleraar.LessonHeader
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.measureTimedValue

@ContributesIntoMap(AppScope::class)
@ViewModelKey(WelcomeViewModel::class)
@Inject
class WelcomeViewModel(
    private val dataService: DataService
) : ViewModel() {
    var items = mutableStateListOf<LessonHeader>()
        private set

    var loading by mutableStateOf(false)
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                items.clear()
                items.addAll(dataService.getLessons())
            } catch (e: Throwable) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                isRefreshing = true
                val (freshList, duration) = measureTimedValue {
                    dataService.getLessons(forceRefresh = true)
                }
                if (duration.inWholeMilliseconds < 300) {
                    // A delay for smoother refresh UX (otherwise the indicator disappears to fast)
                    delay(500 - duration.inWholeMilliseconds)
                }
                items.clear()
                items.addAll(freshList)
            } catch (e: Throwable) {
                error = e.message
            } finally {
                isRefreshing = false
            }
        }
    }
}