package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun FreestyleResultsScreen(
    freestyleResultsViewModel: FreestyleResultsViewModel = viewModel(),
    onSelectMode: () -> Unit,
    onFinish: (Int) -> Unit
) {
    PageHeader(bottomBar = {
        FinishButtonGroup(
            onSelectMode = onSelectMode,
            onFinish = {
                onFinish(freestyleResultsViewModel.accuracyScore.size)
            }
        )
    }) {
        val spacing = LocalSpacing.current
        val accuracy = freestyleResultsViewModel.accuracyScore
        val presentation = freestyleResultsViewModel.presentationScore
        val decrease = freestyleResultsViewModel.stanceDecrease
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
                spacing.small,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(accuracy.size) {
                Box(
                    modifier = Modifier
                        .width(LocalConfiguration.current.screenWidthDp.dp / (accuracy.size * 1.1f))
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    PerformanceResult(
                        columnsPerRow = 4,
                        values = mapOf(
                            "PRECISÃO" to accuracy[it],
                            "APRESENTAÇÃO" to presentation[it],
                            "BASE EXCLUÍDA" to -decrease[it],
                            "NOTA FINAL" to accuracy[it] + presentation[it] - decrease[it]
                        )
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
            controller.navigate(Route.FreestyleScore(count = it))
        })
    }
}