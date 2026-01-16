package com.github.terrakok.nedleraar.ui.lesson
 
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.terrakok.nedleraar.ui.AppTheme
import com.github.terrakok.nedleraar.ui.Icons
import com.github.terrakok.nedleraar.VideoData
import com.github.terrakok.nedleraar.VideoText

@Preview
@Composable
private fun LessonPagePreview() {
    AppTheme {
        LessonPage(
            VideoData(
                "ididid",
                "Ik zou graag een kopje koffie willen bestellen.",
                "https://i.ytimg.com/vi/2afXl60VFlg/sddefault.jpg",
                255,
                listOf(
                    VideoText(0, "Natuurlijk, wil je daar melk en suiker bij? Nee, zwart alsjeblieft."),
                    VideoText(75000, "Hoi Anna, hoe gaat het met je vandaag? Ik zou graag een kopje koffie willen bestellen."),
                    VideoText(84000, "Ik zou graag een kopje koffie willen bestellen."),
                    VideoText(90000, "Natuurlijk, wil je daar melk en suiker bij?"),
                    VideoText(95000, "Nee, zwart alsjeblieft. En een stukje appeltaart."),
                )
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPage(
    videoData: VideoData,
    onBackClick: () -> Unit = {}
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
                            text = "Back to Course",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Bookmark,
                            contentDescription = "Bookmark",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.More,
                            contentDescription = "More",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            var videoProgress by remember { mutableStateOf(0L) }

            Spacer(modifier = Modifier.height(16.dp))
            VideoPlayerPlaceholder(videoData)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "TRANSCRIPT",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(videoData.text) { index, textSegment ->
                    val range = textSegment.timestamp..if (index < videoData.text.lastIndex) videoData.text[index + 1].timestamp else Long.MAX_VALUE
                    val isActive = videoProgress in range
                    TextSegmentItem(textSegment, isActive)
                }
            }
        }
    }
}

@Composable
private fun VideoPlayerPlaceholder(videoData: VideoData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = videoData.previewUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        // Semi-transparent overlay to make controls visible
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
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
}

@Composable
private fun TextSegmentItem(
    textSegment: VideoText,
    isActive: Boolean
) {
    val timestampColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
    val fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { /* Handle click */ }
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
                text = formatTimestamp(textSegment.timestamp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = timestampColor
                ),
                modifier = Modifier.width(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = textSegment.text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = fontWeight,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun formatTimestamp(timestampMs: Long): String {
    val totalSeconds = timestampMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}