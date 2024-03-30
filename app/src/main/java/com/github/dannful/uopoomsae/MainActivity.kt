package com.github.dannful.uopoomsae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.competition_type.competitionTypeRoute
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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
                    NavHost(
                        navController = controller,
                        startDestination = Route.Login.toString()
                    ) {
                        loginRoute(controller)
                        modeSelectScreen(controller)
                        competitionTypeRoute(controller)
                        standardTechniqueScreen(controller, scope)
                        standardPresentationScreen(controller, scope)
                        standardResultsRoute(controller)
                        freestyleScoreRoute(controller, scope)
                        freestyleResultsRoute(controller)
                        scoresReceiverRoute()
                    }
                }
            }
        }
    }
}