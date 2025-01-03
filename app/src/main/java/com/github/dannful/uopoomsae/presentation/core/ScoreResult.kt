package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun ScoreResult(
    score: Float,
    modifier: Modifier = Modifier,
    bodyColor: Color,
    titleText: String
) {
    val spacing = LocalSpacing.current
    Column(modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = Color.White
                )
                .padding(vertical = spacing.tiny, horizontal = spacing.tiny)
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .fillMaxHeight()
                .background(
                    color = bodyColor,
                    shape = RoundedCornerShape(
                        bottomStart = spacing.large,
                        bottomEnd = spacing.large
                    )
                )
                .padding(spacing.large)
        ) {
            Text(
                text = formatDecimal(score),
                style = MaterialTheme.typography.displayLarge,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ScoreBadge(
    score: Float,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(
                vertical = spacing.tiny,
                horizontal = spacing.small
            )
            .background(
                color = badgeColor, shape = RoundedCornerShape(
                    spacing.small
                )
            )
    ) {
        Text(
            text = formatDecimal(score),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}