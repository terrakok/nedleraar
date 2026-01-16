package com.github.terrakok.nedleraar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass

@Composable
internal fun <T : Any> rememberSplitSceneStrategy(): SplitSceneStrategy<T> {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return remember(windowSizeClass) { SplitSceneStrategy(windowSizeClass) }
}

class SplitSceneStrategy<T : Any>(
    private val windowSizeClass: WindowSizeClass
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDE_SIZE)) {
            return null
        }
        val splitIndex = entries.indexOfLast { it.metadata.containsKey(SPLIT_KEY) }
        if (splitIndex == -1) return null
        val rightEntries = entries.subList(splitIndex + 1, entries.size)

        return SplitScene(
            entries.last().contentKey.toString() + entries.size,
            entries[splitIndex],
            rightEntries.lastOrNull(),
            entries.dropLast(1)
        )
    }

    companion object {
        internal const val WIDE_SIZE = 800
        internal const val SPLIT_KEY = "split_strategy_key"
        fun split() = mapOf(SPLIT_KEY to Any())
    }
}

class SplitScene<T : Any>(
    override val key: Any,
    val left: NavEntry<T>,
    val right: NavEntry<T>?,
    override val previousEntries: List<NavEntry<T>>
) : Scene<T> {
    override val entries = listOfNotNull(left, right)
    override val content: @Composable (() -> Unit) = {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxHeight().weight(2f / 5f),
                content = { left.Content() }
            )
            VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Box(
                modifier = Modifier.fillMaxHeight().weight(3f / 5f),
                content = { right?.Content() }
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SplitScene<*>

        if (key != other.key) return false
        if (left != other.left) return false
        if (right != other.right) return false
        if (previousEntries != other.previousEntries) return false
        if (entries != other.entries) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + left.hashCode()
        result = 31 * result + right.hashCode()
        result = 31 * result + previousEntries.hashCode()
        result = 31 * result + entries.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }
}