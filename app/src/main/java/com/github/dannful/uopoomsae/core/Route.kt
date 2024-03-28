package com.github.dannful.uopoomsae.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Route(
    private val name: String,
    val arguments: List<RouteArgument> = emptyList()
) {

    object Login: Route("login")
    object ModeSelect : Route("modeSelect")
    object CompetitionType : Route("competitionType")
    object StandardTechnique : Route("standardTechnique")

    object StandardPresentation : Route(
        "standardPresentation", listOf(
            RouteArgument("techniqueScore")
        )
    )

    object StandardResults : Route(
        "standardResults", listOf(
            RouteArgument("techniqueScore"),
            RouteArgument("presentationScore")
        )
    )

    object FreestyleScore : Route("freestyleScore")
    object FreestyleResults : Route("freestyleResults", listOf(
        RouteArgument("accuracy"),
        RouteArgument("presentation"),
        RouteArgument("stanceDecrease")
    ))

    object ScoresReceiver: Route("scoresReceiver")

    private suspend fun insertArguments(base: String, arguments: Array<out String>): String =
        withContext(Dispatchers.Default) {
            val argumentsIterator = arguments.iterator()
            var optionalAdded = false
            return@withContext buildString {
                append(base)
                this@Route.arguments.forEach { argument ->
                    if (!argumentsIterator.hasNext())
                        return@forEach
                    val next = argumentsIterator.next()
                    if (next.isBlank())
                        return@forEach
                    val optionalArgument = "${argument.name}=$next"
                    append(if (argument.optional) (if (optionalAdded) "&$optionalArgument" else "?$optionalArgument") else "/$next")
                    if (argument.optional)
                        optionalAdded = true
                }
            }
        }

    suspend fun withArguments(vararg arguments: String) =
        insertArguments(name, arguments)

    private fun appendDestinationAndArguments(destination: String) = buildString {
        val mandatoryArguments =
            arguments.filter { !it.optional }.map { it.name }.toTypedArray()
        val optionalArguments = arguments.filter { it.optional }.map { it.name }.toTypedArray()
        append(destination)
        if (mandatoryArguments.isNotEmpty())
            append("/" + mandatoryArguments.joinToString("/") {
                "{$it}"
            })
        if (optionalArguments.isNotEmpty())
            append("?" + optionalArguments.joinToString("&") {
                "$it={$it}"
            })
    }

    override fun toString() = appendDestinationAndArguments(name)
}