package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.dannful.uopoomsae.core.Constants
import com.github.dannful.uopoomsae.core.formatDecimal
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun PerformanceResult(
    columnsPerRow: Int = 3,
    values: Map<String, Float>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnsPerRow),
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
    ) {
        items(values.keys.toList(), key = {
            it
        }) { name ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(Constants.GRID_CELL_SIZE)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    .padding(LocalSpacing.current.small)
            ) {
                val isValueCritical = (values[name] ?: 0f) < 0
                Text(
                    text = name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Divider()
                Box(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatDecimal(values[name] ?: 0f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayLarge,
                        color =
                        if (isValueCritical) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}