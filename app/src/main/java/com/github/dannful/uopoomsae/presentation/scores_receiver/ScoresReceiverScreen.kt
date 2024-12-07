package com.github.dannful.uopoomsae.presentation.scores_receiver

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

private const val CIRCLE_RADIUS = 10f

@Composable
fun ScoresReceiverScreen(
    scoresReceiverViewModel: ScoresReceiverViewModel = hiltViewModel()
) {
    val scores by scoresReceiverViewModel.scores.collectAsState()
    var currentTab by rememberSaveable {
        mutableStateOf(0)
    }
    PageHeader(bottomBar = {
        val scoreBundles = scores[currentTab]
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
            modifier = Modifier.padding(LocalSpacing.current.small)
        ) {
            Button(
                onClick = scoresReceiverViewModel::resetScores,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = scoreBundles.any { it.presentationScore != 0f && it.techniqueScore != 0f }
            ) {
                Text(text = "ZERAR PLACAR")
            }
            val fetchCooldown by scoresReceiverViewModel.lastFetch.collectAsState()
            val isCompetitionMode by scoresReceiverViewModel.isCompetitionMode.collectAsState(
                initial = true
            )
            Button(
                onClick = scoresReceiverViewModel::fetchScores,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                enabled = fetchCooldown == 0 && isCompetitionMode
            ) {
                Text(text = "OBTER DADOS${if (fetchCooldown > 0) " ($fetchCooldown)" else ""}")
            }
        }
    }) {
        TabRow(selectedTabIndex = currentTab) {
            scores.forEachIndexed { index, scoreBundles ->
                Tab(selected = currentTab == index, onClick = {
                    currentTab = index
                }) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                TableHead(text = "PRECISÃO")
                                TableHead(text = "APRESENTAÇÃO")
                            }
                        }
                        item {
                            HorizontalDivider()
                        }
                        items(ScoresReceiverViewModel.JUDGE_COUNT, key = { it }) {
                            val judge = it + 1
                            val recentlyChanged = scoresReceiverViewModel.changes.contains(it)
                            Row {
                                TableHead(
                                    text = "ÁRBITRO $judge",
                                    recentlyChanged = recentlyChanged
                                )
                                TableBody(
                                    text = formatDecimal(scoreBundles[it].techniqueScore),
                                    color = MaterialTheme.colorScheme.onError
                                )
                                TableBody(
                                    text = formatDecimal(scoreBundles[it].presentationScore),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.TableBody(text: String, color: Color) {
    Text(
        text = text,
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = LocalSpacing.current.huge)
            .background(color = color),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun RowScope.TableHead(text: String, recentlyChanged: Boolean = false) {
    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (recentlyChanged)
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                drawCircle(color = Color.Green, radius = CIRCLE_RADIUS)
            }
        Text(
            text = text,
            modifier = Modifier.weight(3f),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}

fun NavGraphBuilder.scoresReceiverRoute() {
    composable<Route.ScoresReceiver> {
        ScoresReceiverScreen()
    }
}