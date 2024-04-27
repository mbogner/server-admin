package dev.mbo.serveradmin.messaging.io.messages

data class RecordStaticMetadata(
    val type: String,
    val schemaVersion: String,
    val contentType: String
)