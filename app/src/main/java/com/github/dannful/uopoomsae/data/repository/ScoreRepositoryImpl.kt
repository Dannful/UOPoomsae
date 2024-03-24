package com.github.dannful.uopoomsae.data.repository

import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.ScoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

class ScoreRepositoryImpl(
    private val networkClient: HttpClient
) : ScoreRepository {

    @Resource(Settings.HTTP_PATH)
    class Scores {

        @Resource("{tableId}")
        class TableId(val parent: Scores = Scores(), val tableId: Int) {

            @Resource("{judgeId}")
            class JudgeId(val parent: TableId, val judgeId: Int)
        }

        @Resource("new")
        class New(val parent: Scores = Scores(), val scoreData: ScoreData)
    }


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

    override suspend fun getScores(tableId: Int): Result<List<ScoreData>> = try {
        val request = networkClient.get(Scores.TableId(tableId = tableId))
        Result.success(request.body<List<ScoreData>>())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendScoreSocket(scoreData: ScoreData): Result<Unit> {
        return try {
            networkClient.webSocket(
                method = HttpMethod.Get,
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

    override suspend fun sendScore(scoreData: ScoreData) = try {
        networkClient.post(Scores.New(scoreData = scoreData))
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}