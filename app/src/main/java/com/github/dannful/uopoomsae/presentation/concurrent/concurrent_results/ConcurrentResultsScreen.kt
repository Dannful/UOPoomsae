package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
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
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                LocalSpacing.current.small,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            PerformanceResult(
                2,
                mapOf(
                    "PRECISÃO 1" to techniqueScore[0],
                    "APRESENTAÇÃO 1" to presentationScore[0],
                ),
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(modifier = Modifier.fillMaxHeight())
            PerformanceResult(
                2,
                mapOf(
                    "PRECISÃO 2" to techniqueScore[1],
                    "APRESENTAÇÃO 2" to presentationScore[1],
                ),
                modifier = Modifier.weight(1f)
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