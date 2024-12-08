package com.github.dannful.uopoomsae.presentation.standard.standard_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.ScoreBundle
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun StandardPresentationScreen(
    standardPresentationViewModel: StandardPresentationViewModel = hiltViewModel(),
    onSend: (ScoreBundle) -> Unit
) {
    val spacing = LocalSpacing.current
    val scores by standardPresentationViewModel.scores.collectAsState()
    PageHeader(bottomBar = {
        SendButton {
            standardPresentationViewModel.send()
            onSend(
                ScoreBundle(
                    techniqueScore = standardPresentationViewModel.previousScore,
                    presentationScore = standardPresentationViewModel.calculateScore()
                )
            )
        }
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.small),
            modifier = Modifier.fillMaxSize()
        ) {
            val speed = scores[0]
            val pace = scores[1]
            val power = scores[2]
            NamedGradient(
                values = StandardPresentationViewModel.values.map { it.toString() },
                name = "VELOCIDADE E POTÊNCIA",
                isSelected = {
                    speed == StandardPresentationViewModel.values[it]
                }
            ) {
                standardPresentationViewModel.setSpeed(
                    StandardPresentationViewModel.values[it]
                )
            }
            NamedGradient(
                values = StandardPresentationViewModel.values.map { it.toString() },
                name = "RITMO E TEMPO",
                isSelected = {
                    pace == StandardPresentationViewModel.values[it]
                }
            ) {
                standardPresentationViewModel.setPace(
                    StandardPresentationViewModel.values[it]
                )
            }
            NamedGradient(
                values = StandardPresentationViewModel.values.map { it.toString() },
                name = "EXPRESSÃO DE ENERGIA",
                isSelected = {
                    power == StandardPresentationViewModel.values[it]
                }
            ) {
                standardPresentationViewModel.setPower(
                    StandardPresentationViewModel.values[it]
                )
            }
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
            onClick = onSelect,
            groupSize = 3
        )
    }
}

fun NavGraphBuilder.standardPresentationScreen(
    controller: NavController
) {
    composable<Route.StandardPresentation> {
        StandardPresentationScreen {
            controller.navigate(
                Route.StandardResults(
                    techniqueScore = it.techniqueScore,
                    presentationScore = it.presentationScore
                )
            )
        }
    }
}