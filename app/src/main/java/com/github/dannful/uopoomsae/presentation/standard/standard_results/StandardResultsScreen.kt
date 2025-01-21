package com.github.dannful.uopoomsae.presentation.standard.standard_results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
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
fun StandardResultsScreen(
    standardResultsViewModel: StandardResultsViewModel = viewModel(),
    onSelectMode: () -> Unit,
    onFinish: () -> Unit
) {
    PageHeader(bottomBar = {
        FinishButtonGroup(
            onSelectMode = onSelectMode,
            onFinish = onFinish
        )
    }, title = "NOTA FINAL") {
        val techniqueScore = standardResultsViewModel.techniqueScore
        val presentationScore = standardResultsViewModel.presentationScore
        Column(
            verticalArrangement = Arrangement.spacedBy(
                LocalSpacing.current.small,
                Alignment.CenterVertically
            ),
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        LocalSpacing.current.huge
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    LocalSpacing.current.huge,
                    Alignment.CenterHorizontally
                )
            ) {
                ScoreResult(
                    score = techniqueScore,
                    bodyColor = Constants.BLUE_COLOR,
                    titleText = "PRECISÃO",
                    modifier = Modifier.weight(1f)
                )
                ScoreResult(
                    score = presentationScore,
                    bodyColor = Constants.BLUE_COLOR,
                    titleText = "APRESENTAÇÃO",
                    modifier = Modifier.weight(1f)
                )
                ScoreBadge(
                    score = techniqueScore + presentationScore,
                    badgeColor = Constants.BLUE_COLOR,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
    }
}

fun NavGraphBuilder.standardResultsRoute(
    controller: NavController
) {
    composable<Route.StandardResults> {
        StandardResultsScreen(onSelectMode = {
            controller.navigate(Route.CompetitionType)
        }, onFinish = {
            controller.navigate(Route.StandardTechnique)
        })
    }
}