package com.github.dannful.uopoomsae.data.repository

import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.resources.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onCompletion

class RemoteRepositoryImpl(
    private val networkClient: Flow<HttpClient>
) : RemoteRepository {

    @Resource(Settings.AUTH_PATH)
    private class Auth

    @Resource(Settings.HTTP_PATH)
    private class Scores {

        @Resource("{tableId}")
        class TableId(val parent: Scores = Scores(), val tableId: Short) {

            @Resource("{judgeId}")
            class JudgeId(val parent: TableId, val judgeId: Short)
        }

        @Resource("new")
        class New(val parent: Scores = Scores(), val scoreData: ScoreData)
    }

    override fun scoresChannel(): Flow<ScoreData> = channelFlow {
        networkClient.onCompletion {
            close()
        }.collectLatest { client ->
            client.webSocket(
                method = HttpMethod.Get,
                host = Settings.SERVER_URL,
                port = Settings.SERVER_PORT,
                path = Settings.SOCKET_PATH
            ) {
                while (true) {
                    val incoming = receiveDeserialized<ScoreData>()
                    send(incoming)
                }
            }
        }
    }

    override fun scoreChannelById(tableId: Short): Flow<ScoreData> = scoresChannel().filter {
        it.tableId == tableId
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserAuth(): Flow<Permissions> = networkClient.mapLatest { client ->
        try {
            val request = client.get(Auth())
            request.body()
        } catch (e: Exception) {
            Permissions.NONE
        } finally {
            client.close()
        }
    }

    override suspend fun getScores(tableId: Short): Result<List<ScoreData>> {
        val client = networkClient.firstOrNull() ?: return Result.failure(NullPointerException())
        return try {
            val request = client.get(Scores.TableId(tableId = tableId))
            Result.success(request.body<List<ScoreData>>())
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            client.close()
        }
    }

    override suspend fun sendScoreSocket(scoreData: ScoreData): Result<Unit> {
        val client = networkClient.firstOrNull() ?: return Result.failure(NullPointerException())
        return try {
            client.webSocket(
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
        } finally {
            client.close()
        }
    }

    override suspend fun sendScore(scoreData: ScoreData): Result<Unit> {
        val client = networkClient.firstOrNull() ?: return Result.failure(NullPointerException())
        return try {
            client.post(Scores.New(scoreData = scoreData))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            client.close()
        }
    }
}