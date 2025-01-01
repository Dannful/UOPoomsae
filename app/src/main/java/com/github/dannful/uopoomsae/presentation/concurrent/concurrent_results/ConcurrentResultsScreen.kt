package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.FinishButtonGroup
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.PerformanceResult
import com.github.dannful.uopoomsae.presentation.core.ScoreBadge
import com.github.dannful.uopoomsae.presentation.core.ScoreResult
import com.github.dannful.uopoomsae.presentation.standard.standard_results.StandardResultsScreen
import com.github.dannful.uopoomsae.presentation.standard.standard_results.StandardResultsViewModel
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun ConcurrentResultsScreen(
    concurrentResultsViewModel: ConcurrentResultsViewModel = viewModel(),
    onSelectMode: () -> Unit,
    onFinish: () -> Unit
) {
    PageHeader(bottomBar = {
        FinishButtonGroup(
            onSelectMode = onSelectMode,
            onFinish = onFinish
        )
    }) {
        val techniqueScore = concurrentResultsViewModel.techniqueScore
        val presentationScore = concurrentResultsViewModel.presentationScore
        Text(
            text = "NOTA FINAL",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f).fillMaxSize(),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .weight(5f)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                LocalSpacing.current.large,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            ResultGroup(
                techniqueScore = techniqueScore[0],
                presentationScore = presentationScore[0],
                color = Color.Red,
                modifier = Modifier.weight(1f)
            )
            ResultGroup(
                techniqueScore = techniqueScore[1],
                presentationScore = presentationScore[1],
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ResultGroup(
    techniqueScore: Float,
    presentationScore: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(2f)
        ) {
            ScoreResult(
                score = techniqueScore,
                bodyColor = color,
                titleText = "PRECISÃO",
                modifier = Modifier.weight(1f),
                textColor = Color.White,
                textStyle = MaterialTheme.typography.displaySmall
            )
            ScoreResult(
                score = presentationScore,
                bodyColor = color,
                titleText = "APRESENTAÇÃO",
                modifier = Modifier.weight(1f),
                textColor = Color.White,
                textStyle = MaterialTheme.typography.displaySmall
            )
        }
        ScoreBadge(
            score = techniqueScore + presentationScore,
            badgeColor = color,
            modifier = Modifier.weight(1f).width(128.dp),
            textColor = Color.White,
            textStyle = MaterialTheme.typography.displaySmall
        )
    }
}

fun NavGraphBuilder.concurrentResultsRoute(
    controller: NavController
) {
    composable<Route.ConcurrentResults> {
        ConcurrentResultsScreen(onSelectMode = {
            controller.navigate(Route.CompetitionType)
        }, onFinish = {
            controller.navigate(Route.ConcurrentTechnique)
        })
    }
}