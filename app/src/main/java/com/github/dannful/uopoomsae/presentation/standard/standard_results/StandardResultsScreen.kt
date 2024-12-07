package com.github.dannful.uopoomsae.presentation.standard.standard_results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.core.FinishButtonGroup
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.PerformanceResult

@Composable
fun StandardResultsScreen(
    standardResultsViewModel: StandardResultsViewModel = viewModel(),
    onSelectMode: () -> Unit,
    onFinish: (Int) -> Unit
) {
    PageHeader(bottomBar = {
        FinishButtonGroup(
            onSelectMode = onSelectMode,
            onFinish = {
                onFinish(standardResultsViewModel.techniqueScore.size)
            }
        )
    }) {
        val techniqueScores = standardResultsViewModel.techniqueScore
        val presentationScores = standardResultsViewModel.presentationScore
        val scores = mutableMapOf<String, Float>()
        for (i in techniqueScores.indices) {
            scores["PRECISÃO ${i + 1}"] = techniqueScores[i]
            scores["APRESENTAÇÃO ${i + 1}"] = presentationScores[i]
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PerformanceResult(
                if (techniqueScores.size == 1) 2 else techniqueScores.size,
                scores
            )
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
            controller.navigate(Route.StandardTechnique(count = it))
        })
    }
}