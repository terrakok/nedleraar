package com.github.terrakok.nedleraar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation

@Composable
internal actual fun BrowserNavigation(backStack: SnapshotStateList<AppNavKey>) {
    HierarchicalBrowserNavigation(
        currentDestination = remember { derivedStateOf { backStack.lastOrNull() } },
        currentDestinationName = {
            when (val key =  it as AppNavKey) {
                is WelcomeScreen -> ""
                is LessonScreen -> "#/lesson/${key.id}"
                is OpenQuestionScreen -> "#/lesson/${key.id}"
            }
        }
    )
}