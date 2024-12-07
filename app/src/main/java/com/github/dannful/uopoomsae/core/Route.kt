package com.github.dannful.uopoomsae.core

import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    data object Login : Route()

    @Serializable
    data object ModeSelect : Route()

    @Serializable
    data object CompetitionType : Route()

    @Serializable
    data class StandardTechnique(
        val count: Int
    ) : Route()

    @Serializable
    data class StandardPresentation(
        val techniqueScores: FloatArray
    ) : Route() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StandardPresentation

            if (!techniqueScores.contentEquals(other.techniqueScores)) return false

            return true
        }

        override fun hashCode(): Int {
            return techniqueScores.contentHashCode()
        }
    }

    @Serializable
    data class StandardResults(
        val techniqueScores: FloatArray,
        val presentationScores: FloatArray
    ) : Route() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StandardResults

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
    data class FreestyleScore(
        val count: Int
    ) : Route()

    @Serializable
    data class FreestyleResults(
        val accuracy: FloatArray,
        val presentation: FloatArray,
        val stanceDecrease: FloatArray
    ) : Route() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FreestyleResults

            if (!accuracy.contentEquals(other.accuracy)) return false
            if (!presentation.contentEquals(other.presentation)) return false
            if (!stanceDecrease.contentEquals(other.stanceDecrease)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = accuracy.contentHashCode()
            result = 31 * result + presentation.contentHashCode()
            result = 31 * result + stanceDecrease.contentHashCode()
            return result
        }
    }

    @Serializable
    data object ScoresReceiver : Route()
}