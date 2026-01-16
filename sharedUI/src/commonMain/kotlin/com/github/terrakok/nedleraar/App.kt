package com.github.terrakok.nedleraar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.github.terrakok.nedleraar.ui.AppTheme
import com.github.terrakok.nedleraar.ui.SplitSceneStrategy
import com.github.terrakok.nedleraar.ui.lesson.LessonPage
import com.github.terrakok.nedleraar.ui.question.OpenQuestionPage
import com.github.terrakok.nedleraar.ui.rememberSplitSceneStrategy
import com.github.terrakok.nedleraar.ui.welcome.WelcomePage

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = WithAppGraph {
    AppTheme(onThemeChanged) {
        val backStack = remember { mutableStateListOf<NavKey>(WelcomeScreen) }

        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val isWide = windowSizeClass.isWide()
        val last = backStack.lastOrNull()
        LaunchedEffect(isWide, last) {
            if (isWide && last is LessonScreen) {
                backStack.add(OpenQuestionScreen(last.id))
            }
        }

        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLowest),
            sceneStrategy = rememberSplitSceneStrategy(),
            entryProvider = entryProvider {
                entry<WelcomeScreen> {
                    WelcomePage(
                        onLessonHeaderClick = { lesson ->
                            backStack.add(LessonScreen(lesson.id))
                        }
                    )
                }
                entry<LessonScreen>(
                    metadata = SplitSceneStrategy.split()
                ) {
                    LessonPage(
                        id = it.id,
                        onBackClick = {
                            backStack.clear()
                            backStack.add(WelcomeScreen)
                        }
                    )
                }
                entry<OpenQuestionScreen> {
                    OpenQuestionPage(
                        id = it.id,
                        onBackClick = { backStack.removeLast() }
                    )
                }
            }
        )
    }
}

private data object WelcomeScreen : NavKey
private data class LessonScreen(val id: String) : NavKey
private data class OpenQuestionScreen(val id: String) : NavKey