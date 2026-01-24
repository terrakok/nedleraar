package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.terrakok.nedleraar.Lesson
import com.github.terrakok.nedleraar.TranscriptionItem
import com.github.terrakok.nedleraar.ui.AppTheme
import com.github.terrakok.nedleraar.ui.Icons
import com.github.terrakok.nedleraar.ui.LoadingWidget
import com.github.terrakok.nedleraar.ui.LocalIsSplitMode
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock

@Preview
@Composable
private fun LessonPagePreview() {
    AppTheme {
        LessonPageContent(
            Lesson(
                id = "1",
                title = "2026-01-11 08:17",
                previewUrl = "https://i.ytimg.com/vi/SgXPCcK22h4/hqdefault.jpg",
                videoId = "",
                videoTranscription = listOf(
                    TranscriptionItem(1, "Hello world!"),
                    TranscriptionItem(2, "How are you?"),
                ),
                lengthSeconds = 0,
                questions = emptyList(),
                createdAt = Clock.System.now(),
                lang = "nl_en"
            )
        )
    }
}

@Composable
fun LessonPage(
    id: String,
    onLearnClick: (id: String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val vm = assistedMetroViewModel<LessonViewModel, LessonViewModel.Factory>(key = id) {
        create(id)
    }
    if (vm.loading || vm.error != null) {
        LoadingWidget(
            modifier = Modifier.fillMaxSize(),
            error = vm.error,
            loading = vm.loading,
            onReload = { vm.loadData() }
        )
        return
    }

    LessonPageContent(
        lesson = vm.lesson!!,
        onLearnClick = { onLearnClick(id) },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonPageContent(
    lesson: Lesson,
    onLearnClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onBackClick() }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Back,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Back to Main",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Translate,
                            contentDescription = "Translate",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                val playerController = remember { YouTubeController() }
                val videoProgress by playerController.progress.receiveAsFlow().collectAsState(0)

                Spacer(modifier = Modifier.height(16.dp))
                VideoPlayerPlaceholder(
                    videoId = lesson.videoId,
                    controller = playerController,
                    previewUrl = lesson.previewUrl
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "TRANSCRIPT",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))

                val state = rememberLazyListState()
                val activeSegmentIndex by remember {
                    derivedStateOf {
                        val second = lesson.videoTranscription.getOrNull(1)?.time ?: Int.MAX_VALUE
                        if (videoProgress < second) {
                            0
                        } else {
                            lesson.videoTranscription.indexOfLast { videoProgress >= it.time }
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
                    itemsIndexed(lesson.videoTranscription) { index, textSegment ->
                        TextSegmentItem(
                            transcriptionItem = textSegment,
                            onClick = { playerController.seekTo(textSegment.time) },
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
            if (!LocalIsSplitMode.current) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 24.dp),
                    onClick = onLearnClick,
                ) {
                    Icon(
                        imageVector = Icons.School,
                        contentDescription = "Practice",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Practice")
                }
            }
        }
    }
}

@Composable
private fun VideoPlayerPlaceholder(
    videoId: String,
    controller: YouTubeController,
    previewUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        if (!controller.play) {
            AsyncImage(
                model = previewUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Play,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp).offset(x = 2.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { controller.play = true }
        )
        if (controller.play) {
            YouTubeWidget(
                videoId = videoId,
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun TextSegmentItem(
    transcriptionItem: TranscriptionItem,
    onClick: () -> Unit,
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
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = timestampColor
                ),
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = transcriptionItem.text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = fontWeight,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun secondsToText(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}"
}