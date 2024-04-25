package dev.mbo.serveradmin.client.cmd.metadata

import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataResponse
import dev.mbo.serveradmin.messaging.sender.ClientSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MetadataService(
    @Value("\${spring.application.name}")
    private val appName: String,
    private val sender: ClientSender,
) {

    fun send() {
        sender.boardToGround(MetadataResponse(appName))
    }

}