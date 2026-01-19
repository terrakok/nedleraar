package com.github.terrakok.nedleraar.ui.question

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.terrakok.nedleraar.ui.Icons
import com.github.terrakok.nedleraar.ui.LoadingWidget
import com.github.terrakok.nedleraar.ui.LocalIsSplitMode
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@Composable
fun OpenQuestionPage(
    id: String,
    onBackClick: () -> Unit = {}
) {
    val vm = assistedMetroViewModel<OpenQuestionViewModel, OpenQuestionViewModel.Factory>(key = id) {
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
    val lesson = vm.lesson ?: return

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            TopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            BottomBar(
                currentQuestionIndex = vm.currentQuestionIndex,
                totalQuestions = lesson.questions.size,
                onPreviousClick = vm::previousQuestion,
                onNextClick = vm::nextQuestion
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = vm.question.text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Medium,
                    lineHeight = 40.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = vm.question.textEn,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Your Answer",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            var answer by vm.answer
            TextField(
                value = answer,
                onValueChange = { answer = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            vm.feedback?.let { FeedbackCard(it) }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        navigationIcon = {
            if (LocalIsSplitMode.current) {
                Row(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LISTENING PRACTICE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            } else {
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
                        text = "Back to Video",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Flag,
                    contentDescription = "Report issue",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
private fun FeedbackCard(feedback: Feedback) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
            )
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Close,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "ALMOST THERE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = feedback.text,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Correct answer:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("Ik zou ")
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("graag")
                                    }
                                    append(" een kopje koffie ")
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("willen")
                                    }
                                    append(" bestellen.")
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Button(
                enabled = currentQuestionIndex > 0,
                onClick = onPreviousClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp).rotate(180f),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text("${currentQuestionIndex + 1} / $totalQuestions")

            Button(
                enabled = currentQuestionIndex < totalQuestions - 1,
                onClick = onNextClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.ArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.weight(1f))

//            if (currentQuestionIndex > 0) {
//                IconButton(
//                    onClick = onPreviousClick,
//                ) {
//                    Icon(
//                        imageVector = Icons.ArrowRight,
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp).rotate(180f),
//                        tint = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//            }
        }
    }
}
