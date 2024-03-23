package com.github.dannful.uopoomsae.presentation.standard.standard_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
import javax.inject.Inject

class StandardResultsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val techniqueScore =
        savedStateHandle.get<Float>(Route.StandardResults.arguments[0].toString()) ?: 0f
    val presentationScore =
        savedStateHandle.get<Float>(Route.StandardResults.arguments[1].toString()) ?: 0f
}