package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.FinishButtonGroup
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.PerformanceResult
import com.github.dannful.uopoomsae.presentation.core.ScoreBadge
import com.github.dannful.uopoomsae.presentation.core.ScoreResult
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun FreestyleResultsScreen(
    freestyleResultsViewModel: FreestyleResultsViewModel = viewModel(),
    onSelectMode: () -> Unit,
    onFinish: () -> Unit
) {
    PageHeader(bottomBar = {
        FinishButtonGroup(
            onSelectMode = onSelectMode,
            onFinish = onFinish
        )
    }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "NOTA FINAL",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            val accuracy = freestyleResultsViewModel.accuracyScore
            val presentation = freestyleResultsViewModel.presentationScore
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(LocalSpacing.current.huge)
                ) {
                    ScoreResult(
                        score = accuracy,
                        bodyColor = Constants.BLUE_COLOR,
                        titleText = "PRECISÃO",
                        modifier = Modifier.weight(1f)
                    )
                    ScoreResult(
                        score = presentation,
                        bodyColor = Constants.BLUE_COLOR,
                        titleText = "APRESENTAÇÃO",
                        modifier = Modifier.weight(1f)
                    )
                    ScoreBadge(
                        score = accuracy + presentation, badgeColor = Constants.BLUE_COLOR,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.freestyleResultsRoute(
    controller: NavController
) {
    composable<Route.FreestyleResults> {
        FreestyleResultsScreen(onSelectMode = {
            controller.navigate(Route.CompetitionType)
        }, onFinish = {
            controller.navigate(Route.FreestyleScore)
        })
    }
}