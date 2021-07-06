package io.paulc03.fabula.server

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import io.paulc03.fabula.server.socket.SocketHandler
import io.paulc03.fabula.server.socket.SocketList
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

typealias EncodedContentType = String
inline class EncodedObject(val content: EncodedContentType)

interface Serializable

fun <T>Serializable.encode(): EncodedObject {
    return EncodedObject(Klaxon().toJsonString(this))
}

inline fun <reified T>EncodedObject.decode(): T? {
    return try {
        Klaxon().parse<T>(content)
    } catch (e: Exception) {
        null
    }
}

@Suppress("unused")
fun Application.module() {
    install(WebSockets)
    install(Koin) {
        slf4jLogger()
    }

    val socketList = SocketList()

    routing {
        webSocket("/world") {
            val socketHandler = SocketHandler(this)
            socketList += socketHandler


            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val content = frame.readText()
                socketHandler.onMessage(EncodedObject(content))
            }

            if (socketList[socketHandler.uid] != null) {
                socketList -= socketHandler
            }
        }
    }
}