package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun FinishButtonGroup(
    modifier: Modifier = Modifier,
    onSelectMode: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small),
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onSelectMode,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "SELECIONAR MODO")
        }
        Button(
            onClick = onFinish,
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "AVALIAR NOVAMENTE")
        }
    }
}