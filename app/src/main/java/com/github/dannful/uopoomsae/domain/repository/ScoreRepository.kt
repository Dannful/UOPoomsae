package com.github.dannful.uopoomsae.domain.repository

import com.github.dannful.uopoomsae.domain.model.ScoreData
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {

    fun scoresChannel(): Flow<ScoreData>
    fun scoreChannelById(tableId: Int): Flow<ScoreData>

    suspend fun sendScore(scoreData: ScoreData): Result<Unit>
}