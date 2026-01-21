package com.github.terrakok.nedleraar

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Instant

@Inject
@SingleIn(AppScope::class)
class DataService(
    private val httpClient: HttpClient
) {
    private val BASE_URL = "https://eymar.nl/lang-practice"
    private val LESSONS_COLLECTION_URL = "$BASE_URL/datav2/"
    private val API_URL = "$BASE_URL/api/"

    private val dispatcher = Dispatchers.Default.limitedParallelism(1)

    private val headers = mutableListOf<LessonHeader>()
    private val lessons = mutableMapOf<String, Lesson>()

    suspend fun getLessons(): List<LessonHeader> = withContext(dispatcher) {
        if (headers.isEmpty()) {
            val json = httpClient.get(LESSONS_COLLECTION_URL + "index.json").body<JsonArray>()
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
            val jo = httpClient.get(LESSONS_COLLECTION_URL + "items/${id}.json").body<JsonObject>()
            val transcription = jo.getValue("transcription").jsonPrimitive.content.splitBySentences()
                .mapIndexed { index, string -> TranscriptionItem(index + 1, string) }
            val practice = jo.getValue("practice").jsonObject
            val questions = practice.getValue("open_questions").jsonArray.mapIndexed { index, element ->
                val q = element.jsonObject
                OpenQuestion(
                    id = q.getValue("id").jsonPrimitive.content,
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
                createdAt = Instant.parse(jo.getValue("createdAt").jsonPrimitive.content.replace(" ", "T")),
                lang = jo.getValue("language").jsonPrimitive.content
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

    suspend fun checkAnswer(
        lessonId: String,
        questionId: String,
        answer: String
    ): String {
        val lang = getLesson(lessonId).lang
        val jo = httpClient.get(API_URL + "check_answer", {
            parameter("lessonId", lessonId)
            parameter("lang", lang)
            parameter("questionId", questionId)
            parameter("answer", answer)
            timeout { requestTimeoutMillis = 60_000  }
        }).body<JsonObject>()

        if (jo.containsKey("error") && jo["error"] != null) {
            return jo.getValue("error").jsonPrimitive.content
        }

        return jo.getValue("result").jsonPrimitive.content
    }
}