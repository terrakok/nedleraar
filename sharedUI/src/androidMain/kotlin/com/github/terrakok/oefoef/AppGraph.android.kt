package com.github.terrakok.oefoef

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@DependencyGraph(AppScope::class)
internal actual interface AppGraph : AppGraphBase