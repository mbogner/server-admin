package dev.mbo.serveradmin.server.command

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.command.Command
import dev.mbo.serveradmin.messaging.io.messages.command.CommandMessage
import dev.mbo.serveradmin.messaging.io.messages.command.CommandPayload
import dev.mbo.serveradmin.messaging.sender.ServerSender
import dev.mbo.serveradmin.server.ServerMetadata
import org.springframework.stereotype.Service

@Service
class CommandService(
    private val sender: ServerSender,
    private val serverMetadata: ServerMetadata,
) {

    private val log = logger()

    fun sendCommands(targets: List<String>, commands: List<Command>) {
        val msg = CommandMessage(
            sender = serverMetadata.name,
            senderKey = serverMetadata.key,
            value = CommandPayload(commands = commands)
        )
        targets.forEach { target ->
            log.debug("sending: {}, value={}", msg, msg.value)
            sender.send(topic = target, message = msg)
        }
    }

}