package com.github.dannful.uopoomsae.presentation.core

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun SendButton(
    modifier: Modifier = Modifier,
    onSend: () -> Unit
) {
    Button(
        onClick = onSend,
        shape = RectangleShape,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "ENVIAR")
    }
}