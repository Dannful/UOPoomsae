package com.github.dannful.uopoomsae.presentation.standard.standard_results

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
    }) {
        Box(
            modifier = Modifier
                .weight(4f)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            PerformanceResult(
                values = mapOf(
                    "PRECISÃO" to standardResultsViewModel.techniqueScore,
                    "APRESENTAÇÃO" to standardResultsViewModel.presentationScore,
                    "NOTA FINAL" to standardResultsViewModel.techniqueScore + standardResultsViewModel.presentationScore
                )
            )
        }
    }
}

fun NavGraphBuilder.standardResultsRoute(
    controller: NavController
) {
    composable(Route.StandardResults.toString(), arguments = listOf(
        navArgument(Route.StandardResults.arguments[0].toString()) {
            type = NavType.FloatType
        },
        navArgument(Route.StandardResults.arguments[1].toString()) {
            type = NavType.FloatType
        }
    )) {
        StandardResultsScreen(onSelectMode = {
            controller.navigate(Route.CompetitionType.toString())
        }, onFinish = {
            controller.navigate(Route.StandardTechnique.toString())
        })
    }
}