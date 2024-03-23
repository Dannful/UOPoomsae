package com.github.dannful.uopoomsae.presentation.core

import android.content.Context
import android.widget.Toast

fun displayRequestFailure(context: Context) {
    Toast.makeText(
        context,
        "Falha ao processar requisição. Por favor, tente novamente mais tarde.",
        Toast.LENGTH_LONG
    ).show()
}