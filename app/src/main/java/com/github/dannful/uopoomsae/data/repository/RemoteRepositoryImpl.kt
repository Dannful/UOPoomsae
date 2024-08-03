package com.github.dannful.uopoomsae.data.repository

import com.github.dannful.uopoomsae.core.Settings
import com.github.dannful.uopoomsae.domain.model.Permissions
import com.github.dannful.uopoomsae.domain.model.ScoreData
import com.github.dannful.uopoomsae.domain.model.UserCredentials
import com.github.dannful.uopoomsae.domain.repository.RemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.resources.Resource

class RemoteRepositoryImpl(
    private val networkClient: HttpClient
) : RemoteRepository {

    @Resource(Settings.AUTH_PATH)
    @Suppress("unused")
    private class Auth {

        @Resource(Settings.AUTH_CURRENT_PATH)
        class Current(val parent: Auth = Auth())

        @Resource(Settings.AUTH_LOGIN_PATH)
        class Login(val parent: Auth = Auth())
    }

    @Resource(Settings.HTTP_PATH)
    private class Scores {

        @Resource("{tableId}")
        @Suppress("unused")
        class TableId(val parent: Scores = Scores(), val tableId: Short) {

            @Resource("{judgeId}")
            class JudgeId(val parent: TableId, val judgeId: Short)
        }
    }

    override suspend fun getUserAuth(): Permissions {
        try {
            val request = networkClient.get(Auth.Current())
            return request.body()
        } catch (e: Exception) {
            return Permissions.NONE
        }
    }

    override suspend fun getScores(tableId: Short): Result<List<ScoreData>> {
        return try {
            val request = networkClient.get(Scores.TableId(tableId = tableId))
            Result.success(request.body<List<ScoreData>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendScore(scoreData: ScoreData): Result<Unit> {
        return try {
            networkClient.post(Scores()) {
                contentType(ContentType.Application.Json)
                setBody(scoreData)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(userCredentials: UserCredentials): Result<Unit> {
        return try {
            networkClient.post(Auth.Login()) {
                contentType(ContentType.Application.Json)
                setBody(userCredentials)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}