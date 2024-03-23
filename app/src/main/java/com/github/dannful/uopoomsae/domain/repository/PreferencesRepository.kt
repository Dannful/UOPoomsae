package com.github.dannful.uopoomsae.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun saveTableId(tableId: Int)
    fun getTableId(): Flow<Int>

    suspend fun setCompetitionMode(competition: Boolean)
    fun getCompetitionMode(): Flow<Boolean>

    suspend fun saveJudgeId(judgeId: Int)
    fun getJudgeId(): Flow<Int>
}