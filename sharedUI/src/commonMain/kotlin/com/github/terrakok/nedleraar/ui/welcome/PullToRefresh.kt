package com.github.terrakok.nedleraar.ui.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput

// It's mostly a copy of androidx.compose.material3.pulltorefresh.PullToRefresh,
// but enabled the PTR gesture only when the app was initially interacted using Touch.
// Reason: PTR when scrolling by mouse wheel doesn't look nice.
@Composable
fun PullToRefreshBoxTouchOnly(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state,
        )
    },
    content: @Composable BoxScope.() -> Unit,
) {
    var isPullToRefreshEnabled by remember { mutableStateOf(false) }
    Box(
        modifier
            .pullToRefresh(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                enabled = isPullToRefreshEnabled
            )
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                    // enabled only when interacted using Touch
                    isPullToRefreshEnabled = event.changes.any { it.type == PointerType.Touch }
                }
            },
        contentAlignment = contentAlignment,
    ) {
        content()
        if (isPullToRefreshEnabled) {
            indicator()
        }
    }
}