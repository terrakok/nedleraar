package com.github.terrakok.oefoef

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraph

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
internal interface WebAppGraph : AppGraphBase

internal actual fun createAppGraph(): AppGraphBase = createGraph<WebAppGraph>()