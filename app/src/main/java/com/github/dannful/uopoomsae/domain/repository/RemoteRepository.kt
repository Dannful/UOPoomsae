package com.github.dannful.uopoomsae.domain.repository

import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    fun scoresChannel(): Flow<ScoreData>
    fun scoreChannelById(tableId: Short): Flow<ScoreData>
    fun getUserAuth(): Flow<Permissions>
    suspend fun getScores(tableId: Short): Result<List<ScoreData>>
    suspend fun sendScoreSocket(scoreData: ScoreData): Result<Unit>
    suspend fun sendScore(scoreData: ScoreData): Result<Unit>
}