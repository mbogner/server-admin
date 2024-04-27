package dev.mbo.serveradmin.messaging.io.messages.command

data class CommandPayload(
    val commands: List<Command> = emptyList(),
)