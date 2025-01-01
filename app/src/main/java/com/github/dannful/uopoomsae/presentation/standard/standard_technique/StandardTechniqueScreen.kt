package com.github.dannful.uopoomsae.presentation.standard.standard_technique

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

private val largeButtonTextStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.displayMedium

private val smallButtonTextStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.bodyLarge

@Composable
fun StandardTechniqueScreen(
    standardTechniqueViewModel: StandardTechniqueViewModel = viewModel(),
    onSend: (Float) -> Unit
) {
    val score by standardTechniqueViewModel.techniqueScore.collectAsState()
    val spacing = LocalSpacing.current
    PageHeader(bottomBar = {
        SendButton {
            onSend(score)
        }
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.small),
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreButton(
                    color = Constants.BLUE_COLOR,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f),
                    value = 0.3f,
                    style = smallButtonTextStyle
                ) {
                    standardTechniqueViewModel.setScore(score + 0.3f)
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = "NOTA PRECISÃO",
                    textAlign = TextAlign.Center
                )
                ScoreButton(
                    color = Constants.BLUE_COLOR,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f),
                    value = 0.1f,
                    style = smallButtonTextStyle
                ) {
                    standardTechniqueViewModel.setScore(score + 0.1f)
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreButton(
                    color = Constants.BLUE_COLOR,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    value = -0.3f,
                    style = largeButtonTextStyle
                ) {
                    standardTechniqueViewModel.setScore(score - 0.3f)
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = formatDecimal(score),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayLarge
                )
                ScoreButton(
                    color = Constants.BLUE_COLOR,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    value = -0.1f,
                    style = largeButtonTextStyle
                ) {
                    standardTechniqueViewModel.setScore(score - 0.1f)
                }
            }
        }
    }
}

@Composable
private fun ScoreButton(
    modifier: Modifier = Modifier,
    color: Color,
    shape: Shape = CircleShape,
    value: Float,
    style: TextStyle,
    setScore: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = shape,
        modifier = modifier,
        onClick = setScore
    ) {
        val plusOrMinus = if (value > 0) "+" else ""
        Text(text = "$plusOrMinus$value", color = Color.Black, style = style)
    }
}

fun NavGraphBuilder.standardTechniqueScreen(
    controller: NavController
) {
    composable<Route.StandardTechnique> {
        StandardTechniqueScreen {
            controller.navigate(
                Route.StandardPresentation(
                    techniqueScore = it
                )
            )
        }
    }
}