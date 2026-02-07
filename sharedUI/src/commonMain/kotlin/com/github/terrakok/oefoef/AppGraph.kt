package com.github.terrakok.oefoef

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import dev.zacsweers.metro.*
import dev.zacsweers.metrox.viewmodel.*
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

@SingleIn(AppScope::class)
internal interface AppGraphBase: ViewModelGraph {
    @SingleIn(AppScope::class)
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        explicitNulls = false
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideSettings(): Settings = Settings()

    @SingleIn(AppScope::class)
    @Provides
    fun provideAppCoroutineScope(): CoroutineScope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClient(json: Json): HttpClient = HttpClient {
        install(ContentNegotiation) { json(json) }
        install(HttpCache)
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.d("httpClient") { message }
                }
            }
            level = if (DEBUG) LogLevel.INFO else LogLevel.NONE
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }
    }
}

internal expect fun createAppGraph(): AppGraphBase

@Composable
internal fun WithAppGraph(
    content: @Composable () -> Unit,
) {
    val graph = remember { createAppGraph() }
    CompositionLocalProvider(
        LocalMetroViewModelFactory provides graph.metroViewModelFactory
    ) {
        content()
    }
}

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
internal class MyViewModelFactory(
    override val viewModelProviders: Map<KClass<out ViewModel>, Provider<ViewModel>>,
    override val assistedFactoryProviders: Map<KClass<out ViewModel>, Provider<ViewModelAssistedFactory>>,
    override val manualAssistedFactoryProviders: Map<KClass<out ManualViewModelAssistedFactory>, Provider<ManualViewModelAssistedFactory>>,
) : MetroViewModelFactory()