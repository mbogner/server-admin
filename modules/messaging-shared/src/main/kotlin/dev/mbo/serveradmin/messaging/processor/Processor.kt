package dev.mbo.serveradmin.messaging.processor

import dev.mbo.serveradmin.logging.logger

abstract class Processor {

    private val log = logger()

    data class RecordStaticMetadata(
        val type: String,
        val schemaVersion: String,
        val contentType: String
    )

    abstract fun supportedRecords(): RecordStaticMetadata

    open fun process(
        topic: String,
        sender: String,
        value: String?,
        headers: Map<String, String>,
        recordId: String,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        log.debug("processing {} message from {} with headers {}: {}", recordStaticMetadata, sender, headers, value)
    }

}