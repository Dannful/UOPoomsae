package com.github.dannful.uopoomsae.presentation.standard.standard_technique

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun StandardTechniqueScreen(
    standardTechniqueViewModel: StandardTechniqueViewModel = viewModel(),
    onSend: (Float) -> Unit
) {
    val score by standardTechniqueViewModel.techniqueScore.collectAsState()
    PageHeader(bottomBar = {
        SendButton {
            onSend(score)
        }
    }) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreButton(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.weight(1f),
                standardTechniqueViewModel = standardTechniqueViewModel,
                value = 0.3f
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "NOTA PRECISÃO",
                textAlign = TextAlign.Center
            )
            ScoreButton(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.weight(1f),
                standardTechniqueViewModel = standardTechniqueViewModel,
                value = 0.1f
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(3f)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreButton(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                standardTechniqueViewModel = standardTechniqueViewModel,
                value = -0.3f
            )
            Text(
                modifier = Modifier.weight(1f),
                text = formatDecimal(score),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = MaterialTheme.typography.displayLarge.fontSize.times(2)
                )
            )
            ScoreButton(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                standardTechniqueViewModel = standardTechniqueViewModel,
                value = -0.1f
            )
        }
    }
}

@Composable
private fun ScoreButton(
    modifier: Modifier = Modifier,
    color: Color,
    shape: Shape = CircleShape,
    standardTechniqueViewModel: StandardTechniqueViewModel,
    value: Float
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = shape,
        modifier = modifier,
        onClick = {
            standardTechniqueViewModel.setScore(standardTechniqueViewModel.techniqueScore.value + value)
        }) {
        val plusOrMinus = if (value > 0) "+" else ""
        Text(text = "$plusOrMinus$value", color = MaterialTheme.colorScheme.onBackground)
    }
}

fun NavGraphBuilder.standardTechniqueScreen(
    controller: NavController,
    scope: CoroutineScope
) {
    composable(Route.StandardTechnique.toString()) {
        StandardTechniqueScreen {
            scope.launch {
                val route = Route.StandardPresentation.withArguments(it.toString())
                controller.navigate(route)
            }
        }
    }
}