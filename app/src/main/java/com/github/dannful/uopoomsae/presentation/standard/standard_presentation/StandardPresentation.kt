package com.github.dannful.uopoomsae.presentation.standard.standard_presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.ScoreBundle
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.presentation.core.displayRequestFailure
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun StandardPresentationScreen(
    standardPresentationViewModel: StandardPresentationViewModel = hiltViewModel(),
    onSend: (ScoreBundle) -> Unit
) {
    PageHeader(bottomBar = {
        val context = LocalContext.current
        SendButton {
            standardPresentationViewModel.send()
            onSend(
                ScoreBundle(
                    techniqueScore = standardPresentationViewModel.previousScore
                        ?: run {
                            displayRequestFailure(context)
                            return@SendButton
                        },
                    presentationScore = standardPresentationViewModel.calculateScore()
                )
            )
        }
    }) {
        val speed by standardPresentationViewModel.speed.collectAsState()
        val pace by standardPresentationViewModel.pace.collectAsState()
        val power by standardPresentationViewModel.power.collectAsState()
        NamedGradient(
            values = StandardPresentationViewModel.values.map { it.toString() },
            name = "VELOCIDADE E POTÊNCIA",
            isSelected = {
                speed == it
            }
        ) {
            standardPresentationViewModel.setSpeed(it)
        }
        NamedGradient(
            values = StandardPresentationViewModel.values.map { it.toString() },
            name = "RITMO E TEMPO",
            isSelected = {
                pace == it
            }
        ) {
            standardPresentationViewModel.setPace(it)
        }
        NamedGradient(
            values = StandardPresentationViewModel.values.map { it.toString() },
            name = "EXPRESSÃO DE ENERGIA",
            isSelected = {
                power == it
            }
        ) {
            standardPresentationViewModel.setPower(it)
        }
    }
}

@Composable
private fun ColumnScope.NamedGradient(
    values: List<String>,
    name: String,
    isSelected: (Int) -> Boolean,
    onSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        ButtonGradient(
            initialColor = Constants.DEFAULT_FIRST_COLOR,
            finalColor = Color.Black,
            values = values,
            isSelected = isSelected,
            onClick = onSelect
        )
    }
}

fun NavGraphBuilder.standardPresentationScreen(
    controller: NavController,
    scope: CoroutineScope
) {
    composable(Route.StandardPresentation.toString(), arguments = listOf(
        navArgument(Route.StandardPresentation.arguments[0].name) {
            type = NavType.FloatType
        }
    )) {
        StandardPresentationScreen {
            scope.launch {
                val route = Route.StandardResults.withArguments(
                    it.techniqueScore.toString(),
                    it.presentationScore.toString()
                )
                controller.navigate(route)
            }
        }
    }
}