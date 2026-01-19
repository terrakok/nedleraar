package com.github.terrakok.nedleraar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
internal actual fun BrowserNavigation(backStack: SnapshotStateList<AppNavKey>) = Unit