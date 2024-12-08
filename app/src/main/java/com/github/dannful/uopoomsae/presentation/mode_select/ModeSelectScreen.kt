package com.github.dannful.uopoomsae.presentation.mode_select

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.core.isInt
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun ModeSelectScreen(
    modeSelectViewModel: ModeSelectViewModel = hiltViewModel(),
    onSend: () -> Unit,
    onReceive: () -> Unit
) {
    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }
    val isUserAdmin by modeSelectViewModel.isUserAdmin.collectAsState(initial = false)
    val competitionMode by modeSelectViewModel.competitionMode.collectAsState(initial = false)
    Scaffold(topBar = {
        if (isUserAdmin) {
            TabRow(
                selectedTabIndex = selectedTab,
            ) {
                Tab(selected = selectedTab == 0, onClick = {
                    selectedTab = 0
                }) {
                    Text(text = "ENVIAR", modifier = Modifier.padding(LocalSpacing.current.medium))
                }
                Tab(selected = selectedTab == 0, onClick = {
                    selectedTab = 1
                }) {
                    Text(text = "RECEBER", modifier = Modifier.padding(LocalSpacing.current.medium))
                }
            }
        }
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        LocalSpacing.current.small
                    ),
                horizontalAlignment = Alignment.End
            ) {
                Switch(checked = competitionMode, onCheckedChange = {
                    modeSelectViewModel.updateCompetitionMode(it)
                })
                Text(text = if (competitionMode) "Competição" else "Treino")
            }
            val judge by modeSelectViewModel.judge.collectAsState()
            val table by modeSelectViewModel.table.collectAsState()
            val isSubmitting by modeSelectViewModel.submitting.collectAsState()
            when (selectedTab) {
                0 -> Send(
                    isSubmitting = isSubmitting,
                    updateJudge = {
                        modeSelectViewModel.setJudgeId(it)
                    },
                    updateTable = {
                        modeSelectViewModel.setTableId(it)
                    },
                    competitionMode = competitionMode,
                    judge = judge,
                    table = table
                ) {
                    modeSelectViewModel.submit(onFinished = onSend)
                }

                1 -> Receive(
                    isSubmitting = isSubmitting, updateTable = {
                        modeSelectViewModel.setTableId(it)
                    }, table = table, competitionMode = competitionMode
                ) {
                    modeSelectViewModel.submit(onFinished = onReceive)
                }
            }
        }
    }
}

@Composable
private fun Send(
    updateJudge: (String) -> Unit,
    updateTable: (String) -> Unit,
    judge: String,
    table: String,
    competitionMode: Boolean,
    isSubmitting: Boolean,
    onSend: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
        LocalSpacing.current.small)) {
        val focusRequester = remember { FocusRequester() }
        val isError = !judge.isInt() || !table.isInt()
        OutlinedTextField(
            value = judge,
            onValueChange = updateJudge,
            isError = !judge.isInt(),
            label = {
                Text(text = "Árbitro")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusRequester.requestFocus()
            }), enabled = competitionMode
        )
        OutlinedTextField(
            value = table,
            onValueChange = updateTable,
            isError = !table.isInt(),
            label = {
                Text(text = "Quadra")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(onSend = {
                if (isError) return@KeyboardActions
                onSend()
            }),
            modifier = Modifier.focusRequester(focusRequester),
            enabled = competitionMode
        )
        if (!isSubmitting) {
            Button(onClick = onSend, enabled = !competitionMode || !isError) {
                Text(text = "ENVIAR")
            }
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Receive(
    updateTable: (String) -> Unit,
    table: String,
    competitionMode: Boolean,
    isSubmitting: Boolean,
    onSend: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(
        LocalSpacing.current.small)) {
        OutlinedTextField(
            value = table,
            onValueChange = updateTable,
            label = {
                Text(text = "Quadra")
            },
            isError = !table.isInt(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(onSend = {
                if (!table.isInt()) return@KeyboardActions
                onSend()
            }), enabled = competitionMode
        )
        if (!isSubmitting) {
            Button(onClick = onSend, enabled = competitionMode && table.isInt()) {
                Text(text = "ENVIAR")
            }
        } else {
            CircularProgressIndicator()
        }
    }
}

fun NavGraphBuilder.modeSelectScreen(navController: NavController) {
    composable<Route.ModeSelect> {
        ModeSelectScreen(
            onSend = { navController.navigate(Route.CompetitionType) },
            onReceive = { navController.navigate(Route.ScoresReceiver) }
        )
    }
}