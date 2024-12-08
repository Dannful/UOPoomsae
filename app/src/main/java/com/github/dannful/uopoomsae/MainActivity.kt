package com.github.dannful.uopoomsae

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.domain.repository.DispatcherProvider
import com.github.dannful.uopoomsae.presentation.competition_type.competitionTypeRoute
import com.github.dannful.uopoomsae.presentation.concurrent.concurrent_presentation.concurrentPresentationScreen
import com.github.dannful.uopoomsae.presentation.concurrent.concurrent_results.concurrentResultsRoute
import com.github.dannful.uopoomsae.presentation.concurrent.concurrent_technique.concurrentTechniqueScreen
import com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results.freestyleResultsRoute
import com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score.freestyleScoreRoute
import com.github.dannful.uopoomsae.presentation.login_screen.loginRoute
import com.github.dannful.uopoomsae.presentation.mode_select.modeSelectScreen
import com.github.dannful.uopoomsae.presentation.scores_receiver.scoresReceiverRoute
import com.github.dannful.uopoomsae.presentation.standard.standard_presentation.standardPresentationScreen
import com.github.dannful.uopoomsae.presentation.standard.standard_results.standardResultsRoute
import com.github.dannful.uopoomsae.presentation.standard.standard_technique.standardTechniqueScreen
import com.github.dannful.uopoomsae.ui.theme.UOReceiverTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UOReceiverTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controller = rememberNavController()
                    val scope = rememberCoroutineScope()
                    val context = LocalContext.current
                    LaunchedEffect(key1 = Unit) {
                        scope.launch(dispatcherProvider.IO) {
                            checkVersion(context)
                        }
                    }
                    NavHost(
                        navController = controller,
                        startDestination = Route.Login
                    ) {
                        loginRoute(controller)
                        modeSelectScreen(controller)
                        competitionTypeRoute(controller)
                        standardTechniqueScreen(
                            controller
                        )
                        standardPresentationScreen(
                            controller
                        )
                        standardResultsRoute(
                            controller
                        )
                        concurrentTechniqueScreen(
                            controller
                        )
                        concurrentPresentationScreen(
                            controller
                        )
                        concurrentResultsRoute(
                            controller
                        )
                        freestyleScoreRoute(controller)
                        freestyleResultsRoute(controller)
                        scoresReceiverRoute()
                    }
                }
            }
        }
    }

    private suspend fun checkVersion(context: Context) {
        try {
            val httpClient = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
            val response =
                httpClient.get(urlString = "https://api.github.com/repos/dannful/UOPoomsae/releases/latest")
                    .body<AppVersionResponse>()
            if (AppVersion.fromString(response.tag_name) > AppVersion.fromString(BuildConfig.VERSION_NAME)) {
                File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ), "uo_poomsae.apk"
                ).apply {
                    if (exists())
                        delete()
                }
                val downloadManager = context.getSystemService(DownloadManager::class.java)
                val asset = response.assets.firstOrNull() ?: return
                val request = DownloadManager.Request(asset.browser_download_url.toUri())
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setTitle("uo_poomsae.apk")
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "uo_poomsae.apk"
                    )
                downloadManager.enqueue(request)
            }
            httpClient.close()
        } catch (_: NoTransformationFoundException) {
            Log.e(
                context.getString(
                    R.string.app_name
                ), "No release versions were found."
            )
        }
    }

    @Serializable
    private data class AppVersionResponse(
        val tag_name: String,
        val assets: List<Asset>
    )

    @Serializable
    private data class Asset(
        val browser_download_url: String
    )

    private data class AppVersion(
        val patch: Int,
        val minor: Int,
        val major: Int
    ) : Comparable<AppVersion> {

        override fun compareTo(other: AppVersion): Int {
            if (major > other.major)
                return 1
            if (major == other.major && minor > other.minor)
                return 1
            if (major == other.major && minor == other.minor && patch > other.patch)
                return 1
            if (major == other.major && minor == other.minor && patch == other.patch)
                return 0
            return -1
        }

        companion object {

            fun fromString(version: String): AppVersion {
                val versionFields = version.split(".")
                return AppVersion(
                    patch = versionFields[2].toInt(),
                    minor = versionFields[1].toInt(),
                    major = versionFields[0].toInt()
                )
            }
        }
    }
}