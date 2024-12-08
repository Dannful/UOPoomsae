package com.github.dannful.uopoomsae.presentation.concurrent.concurrent_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.presentation.concurrent.core.ConcurrentScore
import javax.inject.Inject

class ConcurrentResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val techniqueScore =
        savedStateHandle.get<FloatArray>(Route.ConcurrentResults::techniqueScores.name)!!

    val presentationScore =
        savedStateHandle.get<FloatArray>(Route.ConcurrentResults::presentationScores.name)!!
}