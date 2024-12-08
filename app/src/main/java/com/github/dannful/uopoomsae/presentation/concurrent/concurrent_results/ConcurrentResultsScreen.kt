package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_results

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
import com.github.dannful.uopoomsae.presentation.standard.standard_results.StandardResultsScreen
import com.github.dannful.uopoomsae.presentation.standard.standard_results.StandardResultsViewModel

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
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PerformanceResult(
                3,
                mapOf(
                    "PRECISÃO 1" to techniqueScore[0],
                    "APRESENTAÇÃO 1" to presentationScore[0],
                    "NOTA FINAL 1" to techniqueScore[0] + presentationScore[0],
                    "PRECISÃO 2" to techniqueScore[1],
                    "APRESENTAÇÃO 2" to presentationScore[1],
                    "NOTA FINAL 2" to techniqueScore[1] + presentationScore[1]
                )
            )
        }
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