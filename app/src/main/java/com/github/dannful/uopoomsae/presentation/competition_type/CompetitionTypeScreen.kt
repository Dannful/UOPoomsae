package com.github.dannful.uopoomsae.presentation.competition_type

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.R
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CompetitionTypeScreen(
    competitionTypeViewModel: CompetitionTypeViewModel = hiltViewModel(),
    standardMode: () -> Unit,
    multipleMode: () -> Unit,
    freestyleMode: () -> Unit,
    onBack: (Route) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(LocalSpacing.current.huge)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = {
            competitionTypeViewModel.onUserAuth {
                onBack(if (it) Route.ModeSelect else Route.Login)
            }
        }, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Voltar")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.uopoomsae),
                contentDescription = null,
                modifier = Modifier.weight(1f)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
                modifier = Modifier.weight(1f)
            ) {
                Button(onClick = standardMode, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Simples")
                }
                Button(onClick = multipleMode, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Simultâneo")
                }
                Button(onClick = freestyleMode, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Freestyle")
                }
            }
        }
    }
}

fun NavGraphBuilder.competitionTypeRoute(
    controller: NavController
) {
    composable<Route.CompetitionType> {
        CompetitionTypeScreen(
            standardMode = {
                controller.navigate(Route.StandardTechnique)
            },
            freestyleMode = {
                controller.navigate(Route.FreestyleScore)
            },
            multipleMode = {
                controller.navigate(Route.ConcurrentTechnique)
            },
            onBack = {
                controller.navigate(it)
            }
        )
    }
}