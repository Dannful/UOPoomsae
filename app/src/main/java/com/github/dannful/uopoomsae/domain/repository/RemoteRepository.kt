package com.github.dannful.uopoomsae.domain.repository

import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {

    fun getUserAuth(): Flow<Permissions>
    suspend fun getScores(tableId: Short): Result<List<ScoreData>>
    suspend fun sendScore(scoreData: ScoreData): Result<Unit>
}