package dev.mbo.serveradmin.messaging.io.messages.command

data class Command(
    val up: String,
    val down: String? = null,
)