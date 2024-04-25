package dev.mbo.serveradmin.client.cmd.heartbeat

import dev.mbo.serveradmin.messaging.io.messages.heartbeat.HeartbeatMessage
import dev.mbo.serveradmin.messaging.sender.ClientSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class HeartbeatService(
    @Value("\${spring.application.name}")
    private val appName: String,
    private val sender: ClientSender,
) {

    fun send() {
        sender.boardToGround(HeartbeatMessage(sender = appName))
    }

}