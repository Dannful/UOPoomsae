package com.github.dannful.uopoomsae.core

import com.github.dannful.uopoomsae.presentation.concurrent.core.ConcurrentScore
import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    data object Login : Route()

    @Serializable
    data object ModeSelect : Route()

    @Serializable
    data object CompetitionType : Route()

    @Serializable
    data object StandardTechnique : Route()

    @Serializable
    data class StandardPresentation(
        val techniqueScore: Float
    ) : Route()

    @Serializable
    data class StandardResults(
        val techniqueScore: Float,
        val presentationScore: Float
    ) : Route()

    @Serializable
    data object ConcurrentTechnique : Route()

    @Serializable
    data class ConcurrentPresentation(
        val techniqueScores: FloatArray,
        val reversed: Boolean
    ) : Route() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ConcurrentPresentation

            if (!techniqueScores.contentEquals(other.techniqueScores)) return false

            return true
        }

        override fun hashCode(): Int {
            return techniqueScores.contentHashCode()
        }
    }

    @Serializable
    data class ConcurrentResults(
        val techniqueScores: FloatArray,
        val presentationScores: FloatArray
    ) : Route() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ConcurrentResults

            if (!techniqueScores.contentEquals(other.techniqueScores)) return false
            if (!presentationScores.contentEquals(other.presentationScores)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = techniqueScores.contentHashCode()
            result = 31 * result + presentationScores.contentHashCode()
            return result
        }
    }

    @Serializable
    data object FreestyleScore : Route()

    @Serializable
    data class FreestyleResults(
        val accuracy: Float,
        val presentation: Float,
        val stanceDecrease: Float
    ) : Route()

    @Serializable
    data object ScoresReceiver : Route()
}