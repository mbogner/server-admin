package dev.mbo.serveradmin.client.command

import dev.mbo.serveradmin.client.ClientMetadata
import dev.mbo.serveradmin.client.ServerMetadata
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.command.Command
import dev.mbo.serveradmin.messaging.listener.processor.Processor
import dev.mbo.serveradmin.messaging.sender.ClientSender
import org.springframework.stereotype.Service

@Service
class CommandService(
    private val clientMetadata: ClientMetadata,
    private val serverMetadata: ServerMetadata,
    private val sender: ClientSender,
) {

    private val log = logger()

    fun process(commands: List<Command>, recordMetadata: Processor.RecordMetadata) {
        val undoableCommands = mutableListOf<Command>()
        for (command in commands) {
            try {
                log.debug("processing up command: {}", command.up)
                // TODO run command
                command.down?.let { undoableCommands.add(command) }
            } catch (exc: Exception) {
                log.warn("up command failed: {}", command.up, exc)
                undo(undoableCommands)
            }
        }
    }

    private fun undo(undoableCommands: List<Command>) {
        for (command in undoableCommands.reversed()) {
            try {
                log.debug("processing down command: {}", command.down)
            } catch (exc: Exception) {
                log.warn("down command failed: {}", command.down, exc)
                // TODO do something
            }
        }
    }

}