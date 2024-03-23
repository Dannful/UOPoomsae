package com.github.dannful.uopoomsae.presentation.mode_select

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
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
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            modifier = Modifier
                .wrapContentSize(),
            selectedTabIndex = selectedTab
        ) {
            Tab(selected = selectedTab == 0, onClick = {
                selectedTab = 0
            }, modifier = Modifier.fillMaxWidth().fillMaxHeight(0.15f)) {
                Text(text = "ENVIAR")
            }
            Tab(selected = selectedTab == 0, onClick = {
                selectedTab = 1
            }, modifier = Modifier.fillMaxWidth().fillMaxWidth(0.15f)) {
                Text(text = "RECEBER")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            val competitionMode by modeSelectViewModel.competitionMode.collectAsState(initial = true)
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
            when (selectedTab) {
                0 -> Send(modeSelectViewModel) {
                    modeSelectViewModel.submit()
                    onSend()
                }

                1 -> Receive(modeSelectViewModel) {
                    modeSelectViewModel.submit()
                    onReceive()
                }
            }
        }
    }
}

@Composable
private fun Send(
    modeSelectViewModel: ModeSelectViewModel,
    onSend: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val judge by modeSelectViewModel.judge.collectAsState()
        val table by modeSelectViewModel.table.collectAsState()
        OutlinedTextField(
            value = judge,
            onValueChange = {
                modeSelectViewModel.setJudgeId(it.replace("[^0-9]".toRegex(), ""))
            },
            label = {
                Text(text = "Árbitro")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusRequester.requestFocus()
            })
        )
        OutlinedTextField(
            value = table,
            onValueChange = {
                modeSelectViewModel.setTableId(it.replace("[^0-9]".toRegex(), ""))
            },
            label = {
                Text(text = "Quadra")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(onSend = {
                onSend()
            }),
            modifier = Modifier.focusRequester(focusRequester)
        )
        Button(onClick = onSend) {
            Text(text = "ENVIAR")
        }
    }
}

@Composable
fun Receive(
    modeSelectViewModel: ModeSelectViewModel,
    onSend: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val table by modeSelectViewModel.table.collectAsState()
        OutlinedTextField(
            value = table,
            onValueChange = {
                modeSelectViewModel.setTableId(it.replace("[^0-9]".toRegex(), ""))
            },
            label = {
                Text(text = "Quadra")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(onSend = {
                onSend()
            })
        )
        Button(onClick = onSend) {
            Text(text = "ENVIAR")
        }
    }
}

fun NavGraphBuilder.modeSelectScreen(navController: NavController) {
    composable(Route.ModeSelect.toString()) {
        ModeSelectScreen(
            onSend = { navController.navigate(Route.CompetitionType.toString()) },
            onReceive = { navController.navigate(Route.ScoresReceiver.toString()) }
        )
    }
}