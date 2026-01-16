package com.github.terrakok.nedleraar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    val backStack = remember { mutableStateListOf<NavKey>(LessonScreen(""), OpenQuestionScreen("")) }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLowest),
        sceneStrategy = rememberSplitSceneStrategy(),
        entryProvider = entryProvider {
            entry<LessonScreen>(
                metadata = SplitSceneStrategy.split()
            ) {
                LessonPage(
                    videoData = VideoData(
                        "2afXl60VFlg",
                        "Ik zou graag een kopje koffie willen bestellen.",
                        "https://i.ytimg.com/vi/2afXl60VFlg/sddefault.jpg",
                        255,
                        listOf(
                            VideoText(0, "Natuurlijk, wil je daar melk en suiker bij? Nee, zwart alsjeblieft."),
                            VideoText(
                                75000,
                                "Hoi Anna, hoe gaat het met je vandaag? Ik zou graag een kopje koffie willen bestellen."
                            ),
                            VideoText(84000, "Ik zou graag een kopje koffie willen bestellen."),
                            VideoText(90000, "Natuurlijk, wil je daar melk en suiker bij?"),
                            VideoText(95000, "Nee, zwart alsjeblieft. En een stukje appeltaart."),
                        )
                    ),
                    onBackClick = { if (backStack.size > 1) backStack.removeLast() }
                )
            }
            entry<OpenQuestionScreen> {
                OpenQuestionPage("Translate the phrase spoken by the customer.", 2, 5)
            }
        }
    )
}

private data class LessonScreen(val videoId: String) : NavKey
private data class OpenQuestionScreen(val questionId: String) : NavKey