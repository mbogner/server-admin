package dev.mbo.serveradmin.server.inbound

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.heartbeat.HeartbeatMessage
import dev.mbo.serveradmin.messaging.processor.Processor
import dev.mbo.serveradmin.shared.str.CaseUtil
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class HeartbeatMessageProcessor : Processor() {

    private val log = logger()

    override fun supportedRecords(): RecordStaticMetadata = RecordStaticMetadata(
        type = CaseUtil.toTopicName(HeartbeatMessage::class.java.simpleName),
        schemaVersion = "1",
        contentType = MediaType.APPLICATION_JSON_VALUE
    )

    override fun process(
        topic: String,
        sender: String,
        value: String?,
        headers: Map<String, String>,
        recordId: String,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        super.process(topic, sender, value, headers, recordId, recordStaticMetadata)
        log.debug("TODO") // TODO
    }
}