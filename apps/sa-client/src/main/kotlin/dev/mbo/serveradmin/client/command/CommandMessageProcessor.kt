package dev.mbo.serveradmin.client.command

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.messaging.io.messages.command.CommandMessage
import dev.mbo.serveradmin.messaging.io.messages.command.CommandPayload
import dev.mbo.serveradmin.messaging.listener.processor.Processor
import org.springframework.stereotype.Component

@Component
class CommandMessageProcessor(
    private val service: CommandService,
    private val objectMapper: ObjectMapper,
) : Processor() {

    override fun supportedRecords(): RecordStaticMetadata = CommandMessage.staticMetadata

    override fun process(
        value: String?,
        recordMetadata: RecordMetadata,
        recordStaticMetadata: RecordStaticMetadata,
    ) {
        super.process(value, recordMetadata, recordStaticMetadata)
        if (null == value) return
        val payload = objectMapper.readValue(value, CommandPayload::class.java)
        service.process(payload.commands, recordMetadata)
    }
}