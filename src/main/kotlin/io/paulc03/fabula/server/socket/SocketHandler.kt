package io.paulc03.fabula.server.socket

import com.beust.klaxon.JsonObject
import io.ktor.http.cio.websocket.*
import io.paulc03.fabula.server.EncodedObject
import io.paulc03.fabula.server.decode
import java.util.*
data class BaseProcedureCall(val proc: String?) {

}
class SocketHandler(val session: WebSocketSession) {
    val uid = UUID.randomUUID().toString()



    fun onMessage(message: EncodedObject) {
        val decoded = message.decode<Map<String, Any>>()
        println("${decoded ?: "empty"}")
    }

    override fun toString(): String {
        return "Socket(uid=$uid)"
    }
}