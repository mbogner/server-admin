package dev.mbo.serveradmin.client.cmd.metadata

import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataRequest
import dev.mbo.serveradmin.messaging.listener.processor.Processor
import org.springframework.stereotype.Component

@Component
class MetadataRequestProcessor(
    private val service: MetadataService
) : Processor() {

    override fun supportedRecords(): RecordStaticMetadata = MetadataRequest.staticMetadata

    override fun process(
        value: String?,
        headersRaw: Map<String, String>,
        recordId: String,
        recordMetadata: RecordMetadata,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        super.process(value, headersRaw, recordId, recordMetadata, recordStaticMetadata)
        service.sendMetadata()
    }
}