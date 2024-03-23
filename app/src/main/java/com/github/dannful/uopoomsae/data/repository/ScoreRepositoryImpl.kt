package com.github.dannful.uopoomsae.data.repository

import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.ScoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.deserialize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

class ScoreRepositoryImpl(
    private val networkClient: HttpClient
) : ScoreRepository {

    override fun scoresChannel(): Flow<ScoreData> = flow {
        networkClient.webSocket(
            method = HttpMethod.Get,
            host = Settings.SERVER_URL,
            port = Settings.SERVER_PORT,
            path = Settings.SOCKET_PATH
        ) {
            while (true) {
                val incoming = receiveDeserialized<ScoreData>()
                emit(incoming)
            }
        }
    }

    override fun scoreChannelById(tableId: Int): Flow<ScoreData> = scoresChannel().filter {
        it.tableId == tableId
    }

    override suspend fun sendScore(scoreData: ScoreData): Result<Unit> {
        return try {
            networkClient.webSocket(
                method = HttpMethod.Post,
                host = Settings.SERVER_URL,
                port = Settings.SERVER_PORT,
                path = Settings.SOCKET_PATH
            ) {
                sendSerialized(scoreData)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}