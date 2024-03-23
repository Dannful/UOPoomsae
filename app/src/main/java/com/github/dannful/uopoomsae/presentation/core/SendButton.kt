package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    onSend: () -> Unit
) {
    Button(
        onClick = onSend,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth().padding(LocalSpacing.current.small)
    ) {
        Text(text = "ENVIAR")
    }
}