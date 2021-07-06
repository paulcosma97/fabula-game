package io.paulc03.fabula.server.socket

import io.ktor.http.cio.websocket.*
import io.paulc03.fabula.server.FabException

class SocketList {
    private val list = ArrayList<SocketHandler>()

    operator fun get(uid: String): SocketHandler? {
        return list.find { it.uid == uid }
    }

    operator fun plusAssign(socket: SocketHandler) {
        if (list.contains(socket)) {
            throwSocketConflict(socket)
        }

        list += socket
    }

    suspend operator fun minusAssign(socket: SocketHandler) {
        if (!list.contains(socket)) {
            throwSocketNotFound(socket)
        }

        list -= socket
        close(socket)
    }

    suspend operator fun minusAssign(uid: String) {
        val socket = list.find { it.uid == uid } ?: throwSocketNotFound(uid = uid)
        list -= socket
        close(socket)
    }

    suspend fun close(socket: SocketHandler? = null, uid: String? = null) {
        val toClose = socket?.uid ?: uid ?: throw FabException()
        val sock = get(toClose) ?: throw FabException("Could not find socket $toClose.")
        sock.session.close()
    }

    private fun throwSocketNotFound(socket: SocketHandler? = null, uid: String? = null): Nothing {
        throw FabException("Socket(uid=${socket?.uid ?: uid ?: throw Exception("Either socket or uid params must be set.")}) does not exist in the connection list.")
    }

    private fun throwSocketConflict(socket: SocketHandler): Nothing {
        throw FabSocketInterruptException(InterruptReason.ALREADY_CONNECTED, "$socket already exists in the connection list.")
    }
}