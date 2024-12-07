package com.github.dannful.uopoomsae.presentation.freestyle.freestyle_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
import javax.inject.Inject

class FreestyleResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val accuracyScore =
        savedStateHandle.get<FloatArray>(Route.FreestyleResults::accuracy.name)!!
    val presentationScore =
        savedStateHandle.get<FloatArray>(Route.FreestyleResults::presentation.name)!!
    val stanceDecrease =
        savedStateHandle.get<FloatArray>(Route.FreestyleResults::stanceDecrease.name)!!
}