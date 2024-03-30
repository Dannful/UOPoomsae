package com.github.dannful.uopoomsae.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getUsername(): Flow<String>
    suspend fun saveUsername(username: String)

    fun getPassword(): Flow<String>

    suspend fun savePassword(password: String)
}