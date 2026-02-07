@file:OptIn(ExperimentalWasmJsInterop::class)
@file:JsModule("nspell")

package com.github.terrakok.oefoef.spellcheck

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsArray
import kotlin.js.JsModule
import kotlin.js.JsName
import kotlin.js.JsString

@JsName("default")
external fun nspell(aff: String, dic: String): NSpell

external interface NSpell {
    fun correct(word: String): Boolean
    fun suggest(word: String): JsArray<JsString>
    fun add(word: String): NSpell
    fun remove(word: String): NSpell
}