package com.github.terrakok.oefoef.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.terrakok.oefoef.ui.Icons
import com.github.terrakok.oefoef.ui.LocalIsSplitMode
import kotlinx.coroutines.delay

@Composable
internal fun SyncedTranscriptItemsList(
    videoProgress: Int,
    transcriptItems: List<LessonViewModel.TranscriptItem> = emptyList(),
    onItemClick: (ix: Int) -> Unit,
    onTranslateItemClick: (ix: Int) -> Unit,
) {
    val state = rememberLazyListState()
    val activeSegmentIndex by remember {
        derivedStateOf {
            val second = transcriptItems.getOrNull(1)?.time ?: Int.MAX_VALUE
            if (videoProgress < second) {
                0
            } else {
                transcriptItems.indexOfLast { videoProgress >= it.time }
            }
        }
    }
    LaunchedEffect(activeSegmentIndex) {
        state.animateScrollToItem(
            index = (activeSegmentIndex - 1).coerceAtLeast(0)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state
    ) {
        itemsIndexed(transcriptItems) { index, textSegment ->
            TextSegmentItem(
                transcriptionItem = textSegment,
                onClick = { onItemClick(index) },
                onTranslateClick = { onTranslateItemClick(index) },
                isActive = index == activeSegmentIndex
            )
        }
        item {
            if (!LocalIsSplitMode.current) {
                Spacer(modifier = Modifier.height(100.dp))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TextSegmentItem(
    transcriptionItem: LessonViewModel.TranscriptItem,
    onClick: () -> Unit,
    onTranslateClick: () -> Unit,
    isActive: Boolean
) {
    val timestampColor =
        if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
    val fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent)
            .padding(start = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isActive) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = secondsToText(transcriptionItem.time),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = timestampColor
                ),
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transcriptionItem.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = fontWeight,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                if (transcriptionItem.enTranslation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = transcriptionItem.enTranslation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                    )
                } else if (transcriptionItem.translationInProgress) {
                    Spacer(modifier = Modifier.height(8.dp))
                    TranslationProgressIndicator()
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box {
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = {
                    showMenu = true
                }) {
                    Icon(
                        imageVector = Icons.MenuDots,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("English") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Translate,
                                contentDescription = "Translate",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        onClick = {
                            onTranslateClick()
                            showMenu = false
                        })
                }
            }
        }
    }
}

@Composable
private fun TranslationProgressIndicator(
    base: String = "Translating",
    modifier: Modifier = Modifier
) {
    var dots by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            dots = (dots + 1) % 4 // 0..3
            delay(350)
        }
    }

    Text(
        text = base + ".".repeat(dots),
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        ),
    )
}

private fun secondsToText(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
}