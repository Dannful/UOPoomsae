package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
    PageHeader {
        Box(
            modifier = Modifier
                .weight(5f)
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            PerformanceResult(
                columnsPerRow = 4,
                values = mapOf(
                    "PRECISÃO" to freestyleResultsViewModel.accuracyScore,
                    "APRESENTAÇÃO" to freestyleResultsViewModel.presentationScore,
                    "BASE EXCLUÍDA" to -freestyleResultsViewModel.stanceDecrease,
                    "NOTA FINAL" to freestyleResultsViewModel.accuracyScore + freestyleResultsViewModel.presentationScore - freestyleResultsViewModel.stanceDecrease
                )
            )
        }
        FinishButtonGroup(
            onSelectMode = onSelectMode,
            onFinish = onFinish,
            modifier = Modifier.weight(1f)
        )
    }
}

fun NavGraphBuilder.freestyleResultsRoute(
    controller: NavController
) {
    composable(
        Route.FreestyleResults.toString(), arguments = listOf(
            navArgument(Route.FreestyleResults.arguments[0].toString()) {
                type = NavType.FloatType
            },
            navArgument(Route.FreestyleResults.arguments[1].toString()) {
                type = NavType.FloatType
            },
            navArgument(Route.FreestyleResults.arguments[2].toString()) {
                type = NavType.FloatType
            }
        )) {
        FreestyleResultsScreen(onSelectMode = {
            controller.navigate(Route.CompetitionType.toString())
        }, onFinish = {
            controller.navigate(Route.FreestyleScore.toString())
        })
    }
}