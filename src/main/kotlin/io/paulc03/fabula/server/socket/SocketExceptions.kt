package io.paulc03.fabula.server.socket

import io.paulc03.fabula.server.FabException

enum class InterruptReason {
    ALREADY_CONNECTED,
    RESTART,
    SERVER_ERROR,
}

class FabSocketInterruptException(val reason: InterruptReason, msg: String?) : FabException(msg)