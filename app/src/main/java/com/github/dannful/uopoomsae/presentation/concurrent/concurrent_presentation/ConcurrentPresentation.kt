package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_presentation

import android.content.ContentValues.TAG
import android.util.Log
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
import com.github.dannful.uopoomsae.presentation.concurrent.core.ConcurrentScore
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.ScoreBundle
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
        Box(modifier = Modifier.fillMaxSize()) {
            VerticalDivider(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(
                    spacing.medium,
                    alignment = Alignment.CenterHorizontally
                ),
                modifier = Modifier.align(Alignment.Center)
            ) {
                items(scores.size, key = { it }) { index ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(spacing.small),
                        modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / (scores.size * 1.1f))
                    ) {
                        val speed = scores[index][0]
                        val pace = scores[index][1]
                        val power = scores[index][2]
                        NamedGradient(
                            values = StandardPresentationViewModel.values.map { it.toString() },
                            name = "VELOCIDADE E POTÊNCIA",
                            isSelected = {
                                speed == StandardPresentationViewModel.values[it]
                            }
                        ) {
                            concurrentPresentationViewModel.setValue(
                                index,
                                speed = StandardPresentationViewModel.values[it]
                            )
                        }
                        NamedGradient(
                            values = StandardPresentationViewModel.values.map { it.toString() },
                            name = "RITMO E TEMPO",
                            isSelected = {
                                pace == StandardPresentationViewModel.values[it]
                            }
                        ) {
                            concurrentPresentationViewModel.setValue(
                                index,
                                pace = StandardPresentationViewModel.values[it]
                            )
                        }
                        NamedGradient(
                            values = StandardPresentationViewModel.values.map { it.toString() },
                            name = "EXPRESSÃO DE ENERGIA",
                            isSelected = {
                                power == StandardPresentationViewModel.values[it]
                            }
                        ) {
                            concurrentPresentationViewModel.setValue(
                                index,
                                power = StandardPresentationViewModel.values[it]
                            )
                        }
                    }
                }
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