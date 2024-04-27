package dev.mbo.serveradmin.messaging.listener.processor

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import java.time.Instant
import java.util.*

abstract class Processor {

    private val log = logger()

    data class RecordMetadata(
        val topic: String,
        val ts: Instant,
        val sender: String,
        val senderKey: UUID,
        val targetKey: UUID?
    )

    abstract fun supportedRecords(): RecordStaticMetadata

    open fun process(
        value: String?,
        headersRaw: Map<String, String>,
        recordId: String,
        recordMetadata: RecordMetadata,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        log.debug(
            "processing {} message from {}({}) created at {} with headers {}: {}",
            recordStaticMetadata,
            recordMetadata.sender,
            recordMetadata.senderKey,
            recordMetadata.ts,
            headersRaw,
            value
        )
    }

}