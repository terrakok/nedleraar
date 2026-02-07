package com.github.terrakok.oefoef.spellcheck

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

interface ClientSpellcheck {
    val enabled: Boolean
    suspend fun correct(word: String): Boolean
    suspend fun suggest(word: String): List<String>
}

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
internal class DisabledClientSpellCheck : ClientSpellcheck {
    override val enabled: Boolean
        get() = false

    override suspend fun correct(word: String): Boolean {
        error("DisabledClientSpellCheck doesn't support corrections")
    }

    override suspend fun suggest(word: String): List<String> {
        error("DisabledClientSpellCheck doesn't support suggestions")
    }
}