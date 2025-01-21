package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun ConcurrentPresentationScreen(
    concurrentPresentationViewModel: ConcurrentPresentationViewModel = hiltViewModel(),
    onSend: (FloatArray, FloatArray) -> Unit
) {
    val spacing = LocalSpacing.current
    val scores by concurrentPresentationViewModel.scores.collectAsState()
    PageHeader(bottomBar = {
        SendButton {
            concurrentPresentationViewModel.send()
            onSend(
                concurrentPresentationViewModel.previousScores,
                List(scores.size) { index ->
                    concurrentPresentationViewModel.calculateScore(index)
                }.toFloatArray().let {
                    if (concurrentPresentationViewModel.reversed) it.reversedArray() else it
                }
            )
        }
    }, title = "NOTA DE APRESENTAÇÃO") {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.small, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NamedGradient(
                values = ConcurrentPresentationViewModel.values.map { it.toString() },
                name = "VELOCIDADE E POTÊNCIA",
                reversed = concurrentPresentationViewModel.reversed,
                isFirstSelected = {
                    scores[0][0] == ConcurrentPresentationViewModel.values[it]
                },
                onSelectFirst = {
                    concurrentPresentationViewModel.setValue(
                        0,
                        speed = ConcurrentPresentationViewModel.values[it]
                    )
                },
                isSecondSelected = {
                    scores[1][0] == ConcurrentPresentationViewModel.values[it]
                }
            ) {
                concurrentPresentationViewModel.setValue(
                    1,
                    speed = ConcurrentPresentationViewModel.values[it]
                )
            }
            NamedGradient(
                values = ConcurrentPresentationViewModel.values.map { it.toString() },
                name = "RITMO E TEMPO",
                reversed = concurrentPresentationViewModel.reversed,
                isFirstSelected = {
                    scores[0][1] == ConcurrentPresentationViewModel.values[it]
                },
                onSelectFirst = {
                    concurrentPresentationViewModel.setValue(
                        0,
                        pace = ConcurrentPresentationViewModel.values[it]
                    )
                },
                isSecondSelected = {
                    scores[1][1] == ConcurrentPresentationViewModel.values[it]
                }
            ) {
                concurrentPresentationViewModel.setValue(
                    1,
                    pace = ConcurrentPresentationViewModel.values[it]
                )
            }
            NamedGradient(
                values = ConcurrentPresentationViewModel.values.map { it.toString() },
                name = "EXPRESSÃO DE ENERGIA",
                reversed = concurrentPresentationViewModel.reversed,
                isFirstSelected = {
                    scores[0][2] == ConcurrentPresentationViewModel.values[it]
                },
                onSelectFirst = {
                    concurrentPresentationViewModel.setValue(
                        0,
                        power = ConcurrentPresentationViewModel.values[it]
                    )
                },
                isSecondSelected = {
                    scores[1][2] == ConcurrentPresentationViewModel.values[it]
                }
            ) {
                concurrentPresentationViewModel.setValue(
                    1,
                    power = ConcurrentPresentationViewModel.values[it]
                )
            }
        }
    }
}

@Composable
private fun NamedGradient(
    values: List<String>,
    name: String,
    reversed: Boolean,
    isFirstSelected: (Int) -> Boolean,
    onSelectFirst: (Int) -> Unit,
    isSecondSelected: (Int) -> Boolean,
    onSelectSecond: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.tiny)
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.large)) {
            ButtonGradient(
                initialColor = if (reversed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                finalColor = Color.Black,
                values = values,
                isSelected = isFirstSelected,
                onClick = onSelectFirst,
                groupSize = 3,
                modifier = Modifier.weight(1f),
            )
            ButtonGradient(
                initialColor = if (reversed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                finalColor = Color.Black,
                values = values,
                isSelected = isSecondSelected,
                onClick = onSelectSecond,
                groupSize = 3,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

fun NavGraphBuilder.concurrentPresentationScreen(
    controller: NavController
) {
    composable<Route.ConcurrentPresentation> {
        ConcurrentPresentationScreen { accuracy, presentation ->
            controller.navigate(
                Route.ConcurrentResults(
                    techniqueScores = accuracy,
                    presentationScores = presentation
                )
            )
        }
    }
}