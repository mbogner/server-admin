package dev.mbo.serveradmin.messaging.sender

import dev.mbo.serveradmin.messaging.io.Message

interface ServerSender {
    fun send(topic: String, message: Message<*>)
    fun broadcast(message: Message<*>)
}