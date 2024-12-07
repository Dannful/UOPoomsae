package com.github.dannful.uopoomsae.presentation.standard.standard_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
import javax.inject.Inject

class StandardResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val techniqueScore =
        savedStateHandle.get<FloatArray>(Route.StandardResults::techniqueScores.name)!!
    val presentationScore =
        savedStateHandle.get<FloatArray>(Route.StandardResults::presentationScores.name)!!
}