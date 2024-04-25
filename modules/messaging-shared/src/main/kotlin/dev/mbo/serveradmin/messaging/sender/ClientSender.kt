package dev.mbo.serveradmin.messaging.sender

import dev.mbo.serveradmin.messaging.io.Message

interface ClientSender {
    fun boardToGround(message: Message<*>)
}