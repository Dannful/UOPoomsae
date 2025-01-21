package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_technique

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.R
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.presentation.core.PageHeader
import com.github.dannful.uopoomsae.presentation.core.SendButton
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun ConcurrentTechniqueScreen(
    concurrentTechniqueViewModel: ConcurrentTechniqueViewModel = viewModel(),
    onSend: (FloatArray, Boolean) -> Unit
) {
    val scores by concurrentTechniqueViewModel.techniqueScore.collectAsState()
    val spacing = LocalSpacing.current
    var reversed by rememberSaveable {
        mutableStateOf(false)
    }
    PageHeader(bottomBar = {
        SendButton {
            onSend(if (reversed) scores.reversedArray() else scores, reversed)
        }
    }, title = "NOTA DE PRECISÃO") {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Switch(checked = reversed, onCheckedChange = {
                    reversed = it
                })
                VerticalDivider(
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    spacing.medium,
                    Alignment.CenterHorizontally
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                ScoreSet(reversed = reversed, left = true, score = scores[0], updateScore = {
                    concurrentTechniqueViewModel.setScore(
                        firstScore = it
                    )
                })
                ScoreSet(reversed = reversed, left = false, score = scores[1], updateScore = {
                    concurrentTechniqueViewModel.setScore(
                        secondScore = it
                    )
                })
            }
        }
    }
}

private fun getArrow(colorIndex: Int, left: Boolean): Int {
    if (left)
        return if (colorIndex == 0) R.drawable.vermelho_esquerda else R.drawable.azul_esquerda
    return if (colorIndex == 0) R.drawable.vermelho_direita else R.drawable.azul_direita
}

@Composable
private fun RowScope.ScoreSet(
    left: Boolean,
    reversed: Boolean,
    score: Float,
    updateScore: (Float) -> Unit,
) {
    val spacing = LocalSpacing.current
    val colors =
        listOf(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.primaryContainer)
    val colorIndex = ((if (left) 0 else 1) + (if (reversed) 1 else 0)) % colors.size
    val colorAnimation by animateColorAsState(
        targetValue = colors[colorIndex],
        label = "buttonColor"
    )
    Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        val firstBlock = @Composable {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.small),
                modifier = Modifier.Companion
                    .weight(2f)
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.small),
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val text =
                        @Composable {
                            Text(
                                text = formatDecimal(score), textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                    val button = @Composable {
                        ScoreButton(
                            color = colorAnimation,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            value = -0.3f
                        ) {
                            updateScore(score - 0.3f)
                        }
                    }
                    if (left) {
                        button()
                        text()
                    } else {
                        text()
                        button()
                    }
                }
                ScoreButton(
                    color = colorAnimation,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxSize(),
                    value = -0.1f,
                ) {
                    updateScore(score - 0.1f)
                }
            }
        }
        val arrowBlock = @Composable {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = getArrow(colorIndex, left)),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(spacing.large),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Inside
                )
                Image(
                    painter = painterResource(id = getArrow(colorIndex, left)),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(spacing.large),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Inside
                )
                ScoreButton(
                    color = colorAnimation,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    value = 0.1f
                ) {
                    updateScore(score + 0.1f)
                }
            }
        }
        if (left) {
            firstBlock()
            arrowBlock()
        } else {
            arrowBlock()
            firstBlock()
        }
    }
}

@Composable
private fun ScoreButton(
    modifier: Modifier = Modifier,
    color: Color,
    shape: Shape = CircleShape,
    value: Float,
    setScore: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = shape,
        modifier = modifier,
        onClick = setScore
    ) {
        val plusOrMinus = if (value > 0) "+" else ""
        Text(text = "$plusOrMinus$value", color = MaterialTheme.colorScheme.onBackground)
    }
}

fun NavGraphBuilder.concurrentTechniqueScreen(
    controller: NavController
) {
    composable<Route.ConcurrentTechnique> {
        ConcurrentTechniqueScreen { values, reversed ->
            controller.navigate(
                Route.ConcurrentPresentation(
                    values, reversed
                )
            )
        }
    }
}