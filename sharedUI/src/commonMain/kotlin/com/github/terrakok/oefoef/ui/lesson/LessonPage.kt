package com.github.terrakok.oefoef.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.terrakok.oefoef.Lesson
import com.github.terrakok.oefoef.TranscriptionItem
import com.github.terrakok.oefoef.ui.AppTheme
import com.github.terrakok.oefoef.ui.Icons
import com.github.terrakok.oefoef.ui.LoadingWidget
import com.github.terrakok.oefoef.ui.LocalIsSplitMode
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.coroutines.flow.receiveAsFlow
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
        transcriptItems = vm.transcriptItems,
        onTranslateTranscriptItemClick = {
            vm.translateTranscriptItem(it)
        },
        onLearnClick = { onLearnClick(id) },
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonPageContent(
    lesson: Lesson,
    transcriptItems: List<LessonViewModel.TranscriptItem> = emptyList(),
    onTranslateTranscriptItemClick: (ix: Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onLearnClick: () -> Unit = {}
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.navigationBars),
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
                actions = {}
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

                SyncedTranscriptItemsList(
                    videoProgress = videoProgress,
                    transcriptItems = transcriptItems,
                    onItemClick = { ix ->
                        playerController.seekTo(transcriptItems[ix].time)
                    },
                    onTranslateItemClick = { ix ->
                        onTranslateTranscriptItemClick(ix)
                    }
                )
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
