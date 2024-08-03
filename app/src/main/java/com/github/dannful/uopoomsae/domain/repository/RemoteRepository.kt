package com.github.dannful.uopoomsae.domain.repository

import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.model.UserCredentials

interface RemoteRepository {

    suspend fun getUserAuth(): Permissions
    suspend fun getScores(tableId: Short): Result<List<ScoreData>>
    suspend fun sendScore(scoreData: ScoreData): Result<Unit>
    suspend fun login(userCredentials: UserCredentials): Result<Unit>
}