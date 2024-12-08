package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results

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
        val accuracy = freestyleResultsViewModel.accuracyScore
        val presentation = freestyleResultsViewModel.presentationScore
        val decrease = freestyleResultsViewModel.stanceDecrease
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PerformanceResult(
                columnsPerRow = 4,
                values = mapOf(
                    "PRECISÃO" to accuracy,
                    "APRESENTAÇÃO" to presentation,
                    "BASE EXCLUÍDA" to -decrease,
                    "NOTA FINAL" to accuracy + presentation - decrease
                )
            )
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