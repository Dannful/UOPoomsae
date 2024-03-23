package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.ButtonGradient
import com.github.dannful.uopoomsae.presentation.core.FreestyleScores
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FreestyleScoreScreen(
    freestyleScoreViewModel: FreestyleScoreViewModel = hiltViewModel(),
    onSend: (FreestyleScores) -> Unit
) {
    PageHeader(bottomBar = {
        SendButton {
            freestyleScoreViewModel.send()
            onSend(
                FreestyleScores(
                    presentation = freestyleScoreViewModel.calculatePresentation(),
                    accuracy = freestyleScoreViewModel.calculateTechnique(),
                    stanceDecrease = freestyleScoreViewModel.calculateStanceDecrease()
                )
            )
        }
    }) {
        Column(
            modifier = Modifier
                .weight(6f)
                .verticalScroll(rememberScrollState())
        ) {
            val scores by freestyleScoreViewModel.scores.collectAsState()
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
                    scores[index] == it
                }) {
                    freestyleScoreViewModel.setScoreIndex(index, it)
                }
            }
            CheckGroup(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                freestyleScoreViewModel = freestyleScoreViewModel
            )
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
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            ButtonGradient(
                horizontalArrangement = Arrangement.End,
                boxSize = 45.dp,
                initialColor = Constants.DEFAULT_FIRST_COLOR,
                finalColor = Color.Black,
                values = FreestyleScoreViewModel.values.map { it.toString() },
                isSelected = isSelected, onClick = onClick
            )
        }
        HorizontalDivider()
    }
}

@Composable
fun CheckGroup(
    modifier: Modifier = Modifier,
    freestyleScoreViewModel: FreestyleScoreViewModel
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val firstStance by freestyleScoreViewModel.firstStance.collectAsState()
        val secondStance by freestyleScoreViewModel.secondStance.collectAsState()
        val thirdStance by freestyleScoreViewModel.thirdStance.collectAsState()
        NamedCheckbox(
            text = "HAKDARI SEOGI",
            isChecked = firstStance
        ) {
            freestyleScoreViewModel.setFirstStance(it)
        }
        NamedCheckbox(
            text = "BEOM SEOGI",
            isChecked = secondStance
        ) {
            freestyleScoreViewModel.setSecondStance(it)
        }
        NamedCheckbox(
            text = "DWIT KUBI SEOGI",
            isChecked = thirdStance
        ) {
            freestyleScoreViewModel.setThirdStance(it)
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
    controller: NavController,
    scope: CoroutineScope
) {
    composable(Route.FreestyleScore.toString()) {
        FreestyleScoreScreen {
            scope.launch {
                val route = Route.FreestyleResults.withArguments(
                    it.accuracy.toString(),
                    it.presentation.toString(),
                    it.stanceDecrease.toString()
                )
                controller.navigate(route)
            }
        }
    }
}