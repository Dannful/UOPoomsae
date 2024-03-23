package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
import javax.inject.Inject

class FreestyleResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val accuracyScore = savedStateHandle.get<Float>(Route.FreestyleResults.arguments[0].toString()) ?: 0f
    val presentationScore = savedStateHandle.get<Float>(Route.FreestyleResults.arguments[1].toString()) ?: 0f
    val stanceDecrease = savedStateHandle.get<Float>(Route.FreestyleResults.arguments[2].toString()) ?: 0f
}