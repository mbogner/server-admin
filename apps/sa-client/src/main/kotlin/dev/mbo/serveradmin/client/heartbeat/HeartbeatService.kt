package dev.mbo.serveradmin.client.heartbeat

import dev.mbo.serveradmin.client.ClientMetadata
import dev.mbo.serveradmin.client.ServerMetadata
import dev.mbo.serveradmin.messaging.io.messages.heartbeat.HeartbeatMessage
import dev.mbo.serveradmin.messaging.sender.ClientSender
import org.springframework.stereotype.Service

@Service
class HeartbeatService(
    private val clientMetadata: ClientMetadata,
    private val serverMetadata: ServerMetadata,
    private val sender: ClientSender,
) {

    fun sendHeartbeat() {
        sender.boardToGround(
            HeartbeatMessage(
                sender = clientMetadata.name,
                senderKey = clientMetadata.key,
                targetKey = serverMetadata.key
            )
        )
    }

}