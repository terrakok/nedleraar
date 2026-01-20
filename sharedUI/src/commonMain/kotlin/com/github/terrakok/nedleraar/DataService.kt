package com.github.terrakok.nedleraar

import androidx.compose.runtime.mutableStateListOf
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlin.time.Instant

@Inject
@SingleIn(AppScope::class)
class DataService(
    private val httpClient: HttpClient
) {
    private val API_URL = "https://eymar.nl/lang-practice/datav2/"
    private val dispatcher = Dispatchers.Default.limitedParallelism(1)

    private val headers = mutableListOf<LessonHeader>()
    private val lessons = mutableMapOf<String, Lesson>()

    suspend fun getLessons(): List<LessonHeader> = withContext(dispatcher) {
        if (headers.isEmpty()) {
            val json = httpClient.get(API_URL + "index.json").body<JsonArray>()
            val new = json.map {
                val jo = it.jsonObject
                LessonHeader(
                    id = jo.getValue("id").jsonPrimitive.content,
                    title = jo.getValue("title").jsonPrimitive.content,
                    previewUrl = jo.getValue("pictureUrl").jsonPrimitive.content,
                    lengthSeconds = jo["videoDuration"]?.jsonPrimitive?.int ?: 0,
                    createdAt = Instant.parse(jo.getValue("createdAt").jsonPrimitive.content.replace(" ", "T"))
                )
            }.sortedByDescending { it.createdAt }
            headers.addAll(new)
        }
        headers
    }

    suspend fun getLesson(id: String): Lesson = withContext(dispatcher) {
        lessons.getOrPut(id) {
            val jo = httpClient.get(API_URL + "items/${id}.json").body<JsonObject>()
            val transcription = jo.getValue("transcription").jsonPrimitive.content.splitBySentences()
                .mapIndexed { index, string -> TranscriptionItem(index + 1, string) }
            val practice = jo.getValue("practice").jsonObject
            val questions = practice.getValue("open_questions").jsonArray.mapIndexed { index, element ->
                val q = element.jsonObject
                OpenQuestion(
                    id = index.toString(),
                    text = q.getValue("question").jsonPrimitive.content,
                    textEn = q.getValue("question_en").jsonPrimitive.content
                )
            }
            Lesson(
                id = id,
                videoId = jo.getValue("videoId").jsonPrimitive.content,
                title = jo.getValue("title").jsonPrimitive.content,
                previewUrl = jo.getValue("pictureUrl").jsonPrimitive.content,
                lengthSeconds = jo.getValue("videoDuration").jsonPrimitive.int,
                videoTranscription = transcription,
                questions = questions,
                createdAt = Instant.parse(jo.getValue("createdAt").jsonPrimitive.content.replace(" ", "T"))
            )
        }
    }

    private fun String.splitBySentences(): List<String> {
        val result = mutableListOf<String>()
        var lastStart = 0
        for (i in indices) {
            if (this[i] in ".!?") {
                val sentence = substring(lastStart, i + 1).trim()
                if (sentence.isNotEmpty()) result.add(sentence)
                lastStart = i + 1
            }
        }
        val remaining = substring(lastStart).trim()
        if (remaining.isNotEmpty()) result.add(remaining)
        return result
    }
}