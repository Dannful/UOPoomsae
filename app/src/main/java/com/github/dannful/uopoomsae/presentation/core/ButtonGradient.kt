package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.dannful.uopoomsae.core.color.interpolateColor
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun ButtonGradient(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    boxSize: Dp = 48.dp,
    initialColor: Color,
    finalColor: Color,
    groupSize: Int,
    values: List<String>,
    isSelected: (Int) -> Boolean,
    onClick: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(values,
            key = { index, _ -> index }) { index, item ->
            val maxIndex = values.size / groupSize
            val group = index / groupSize
            val red = interpolateColor(initialColor.red, finalColor.red, group, maxIndex)
            val green = interpolateColor(initialColor.green, finalColor.green, group, maxIndex)
            val blue = interpolateColor(initialColor.blue, finalColor.blue, group, maxIndex)
            val color =
                Color(red.coerceIn(0f..255f), green.coerceIn(0f..255f), blue.coerceIn(0f..255f))
            val animatedColor by
            animateColorAsState(
                targetValue = if (isSelected(index)) Color.Gray else color,
                label = "chip$index"
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(boxSize)
                    .border(
                        shape = RectangleShape,
                        color = MaterialTheme.colorScheme.onBackground,
                        width = 1.dp
                    )
                    .background(color = animatedColor)
                    .clickable {
                        onClick(index)
                    }
                    .padding(LocalSpacing.current.small)
            ) {
                Text(text = item, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}