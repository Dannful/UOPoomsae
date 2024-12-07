package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.FreestyleScores
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.presentation.standard.standard_presentation.standardPresentationScreen
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FreestyleScoreScreen(
    freestyleScoreViewModel: FreestyleScoreViewModel = hiltViewModel(),
    onSend: (FreestyleScores) -> Unit
) {
    val spacing = LocalSpacing.current
    val scores by freestyleScoreViewModel.scores.collectAsState()
    PageHeader(bottomBar = {
        SendButton {
            freestyleScoreViewModel.send()
            onSend(
                FreestyleScores(
                    presentation = List(scores.size) { index ->
                        freestyleScoreViewModel.calculatePresentation(
                            index
                        )
                    },
                    accuracy = List(scores.size) { index ->
                        freestyleScoreViewModel.calculateTechnique(
                            index
                        )
                    },
                    stanceDecrease = List(scores.size) { index ->
                        freestyleScoreViewModel.calculateStanceDecrease(
                            index
                        )
                    }
                )
            )
        }
    }) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
                spacing.small,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            items(scores.size) { scoreIndex ->
                Column(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp / (scores.size * 1.1f))
                        .verticalScroll(rememberScrollState())
                ) {
                    val score = scores[scoreIndex]
                    listOf(
                        "LATERAL SALTANDO",
                        "FRONTAL SEQUENCIAL SALTANDO",
                        "GRADIENTE SALTANDO",
                        "CONSECUTIVO DE KYORUGI",
                        "ACROBACIA",
                        "MOVIMENTOS PRÁTICOS",
                        "CRIATIVIDADE",
                        "HARMONIA",
                        "EXPRESSÃO DE ENERGIA",
                        "MÚSICA E COREOGRAFIA"
                    ).forEachIndexed { index, name ->
                        NamedButtonGradient(name = name, isSelected = {
                            score.scores[index] == FreestyleScoreViewModel.values[it]
                        }) {
                            freestyleScoreViewModel.setScoreIndex(
                                scoreIndex,
                                index,
                                FreestyleScoreViewModel.values[it]
                            )
                        }
                    }
                    CheckGroup(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        freestyleScoreViewModel = freestyleScoreViewModel,
                        index = scoreIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun NamedButtonGradient(
    name: String,
    isSelected: (Int) -> Boolean,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, style = MaterialTheme.typography.titleSmall)
            ButtonGradient(
                horizontalArrangement = Arrangement.End,
                boxSize = 45.dp,
                initialColor = Constants.DEFAULT_FIRST_COLOR,
                finalColor = Color.Black,
                values = FreestyleScoreViewModel.values.map { it.toString() },
                isSelected = isSelected, onClick = onClick, groupSize = 2
            )
        }
        HorizontalDivider()
    }
}

@Composable
fun CheckGroup(
    modifier: Modifier = Modifier,
    index: Int,
    freestyleScoreViewModel: FreestyleScoreViewModel
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scores by freestyleScoreViewModel.scores.collectAsState()
        val firstStance = scores[index].firstStance
        val secondStance = scores[index].secondStance
        val thirdStance = scores[index].thirdStance
        NamedCheckbox(
            text = "HAKDARI SEOGI",
            isChecked = firstStance
        ) {
            freestyleScoreViewModel.setFirstStance(index, it)
        }
        NamedCheckbox(
            text = "BEOM SEOGI",
            isChecked = secondStance
        ) {
            freestyleScoreViewModel.setSecondStance(index, it)
        }
        NamedCheckbox(
            text = "DWIT KUBI SEOGI",
            isChecked = thirdStance
        ) {
            freestyleScoreViewModel.setThirdStance(index, it)
        }
    }
}

@Composable
private fun NamedCheckbox(text: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

fun NavGraphBuilder.freestyleScoreRoute(
    controller: NavController
) {
    composable<Route.FreestyleScore> {
        FreestyleScoreScreen {
            controller.navigate(
                Route.FreestyleResults(
                    accuracy = it.accuracy.toFloatArray(),
                    presentation = it.presentation.toFloatArray(),
                    stanceDecrease = it.stanceDecrease.toFloatArray()
                )
            )
        }
    }
}