package dev.mbo.serveradmin.server.metadata

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataMessage
import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataPayload
import dev.mbo.serveradmin.messaging.listener.processor.Processor
import dev.mbo.serveradmin.server.db.client.ClientService
import org.springframework.stereotype.Component

@Component
class MetadataMessageProcessor(
    private val objectMapper: ObjectMapper,
    private val clientService: ClientService
) : Processor() {

    private val log = logger()

    override fun supportedRecords(): RecordStaticMetadata = MetadataMessage.staticMetadata

    override fun process(
        value: String?,
        recordMetadata: RecordMetadata,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        super.process(value, recordMetadata, recordStaticMetadata)
        validate(recordStaticMetadata)
        try {
            if (value.isNullOrBlank()) {
                throw IllegalArgumentException("Value must not be null or blank")
            }

            // parse value for validation
            objectMapper.readValue(value, MetadataPayload::class.java)
            log.debug("received valid metadata {}", recordStaticMetadata)

            clientService.storeMetadata(
                ts = recordMetadata.ts,
                sender = recordMetadata.sender,
                senderKey = recordMetadata.senderKey,
                metadata = value,
                schema = recordStaticMetadata.type,
                schemaVersion = recordStaticMetadata.schemaVersion,
            )
        } catch (exc: JacksonException) {
            throw IllegalArgumentException("Unable to deserialize metadata", exc)
        }
    }

    private fun validate(recordStaticMetadata: RecordStaticMetadata) {
        if (recordStaticMetadata.type != MetadataMessage.staticMetadata.type) {
            throw IllegalArgumentException("Wrong metadata type: ${recordStaticMetadata.type}")
        }
        if (recordStaticMetadata.contentType != MetadataMessage.staticMetadata.contentType) {
            throw IllegalArgumentException("Wrong content type: ${recordStaticMetadata.contentType}")
        }
        if (recordStaticMetadata.schemaVersion != MetadataMessage.staticMetadata.schemaVersion) {
            throw IllegalArgumentException("Wrong schema version: ${recordStaticMetadata.schemaVersion}")
        }
    }
}