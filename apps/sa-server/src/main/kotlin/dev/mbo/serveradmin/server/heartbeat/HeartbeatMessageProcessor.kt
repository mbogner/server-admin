package dev.mbo.serveradmin.server.heartbeat

import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.messaging.io.messages.heartbeat.HeartbeatMessage
import dev.mbo.serveradmin.messaging.listener.processor.Processor
import dev.mbo.serveradmin.server.db.client.ClientService
import org.springframework.stereotype.Component

@Component
class HeartbeatMessageProcessor(
    private val clientService: ClientService,
) : Processor() {

    override fun supportedRecords(): RecordStaticMetadata = HeartbeatMessage.staticMetadata

    override fun process(
        value: String?,
        headersRaw: Map<String, String>,
        recordId: String,
        recordMetadata: RecordMetadata,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        super.process(value, headersRaw, recordId, recordMetadata, recordStaticMetadata)
        clientService.storeHeartbeat(recordMetadata.ts, recordMetadata.sender, recordMetadata.senderKey)
    }
}