package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.presentation.standard.standard_presentation.StandardPresentationViewModel
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
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.small),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDecimal(scores[0].sum()),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "NOTA DE APRESENTAÇÃO",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = formatDecimal(scores[1].sum()),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            NamedGradient(
                values = ConcurrentPresentationViewModel.values.map { it.toString() },
                name = "VELOCIDADE E POTÊNCIA",
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
                },
                onSelectSecond = {
                    concurrentPresentationViewModel.setValue(
                        1,
                        speed = ConcurrentPresentationViewModel.values[it]
                    )
                },
                reversed = concurrentPresentationViewModel.reversed
            )
            NamedGradient(
                values = ConcurrentPresentationViewModel.values.map { it.toString() },
                name = "RITMO E TEMPO",
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
                },
                onSelectSecond = {
                    concurrentPresentationViewModel.setValue(
                        1,
                        pace = ConcurrentPresentationViewModel.values[it]
                    )
                },
                reversed = concurrentPresentationViewModel.reversed
            )
            NamedGradient(
                values = ConcurrentPresentationViewModel.values.map { it.toString() },
                name = "EXPRESSÃO DE ENERGIA",
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
                },
                onSelectSecond = {
                    concurrentPresentationViewModel.setValue(
                        1,
                        power = ConcurrentPresentationViewModel.values[it]
                    )
                },
                reversed = concurrentPresentationViewModel.reversed
            )
        }
    }
}

@Composable
private fun ColumnScope.NamedGradient(
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