package dev.mbo.serveradmin.messaging.sender

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.SharedTopic
import dev.mbo.serveradmin.messaging.io.Message
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.http.MediaType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class MessageSender(
    private val kafka: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) : ClientSender, ServerSender {

    private val log = logger()

    override fun broadcast(message: Message<*>) {
        send(SharedTopic.CLIENT_BROADCAST, message)
    }

    override fun boardToGround(message: Message<*>) {
        send("${SharedTopic.BOARD_TO_GROUND}.${message.type}", message)
    }

    override fun send(topic: String, message: Message<*>) {
        val record = ProducerRecord(
            topic,
            null,
            message.createdAt.toEpochMilli(),
            message.sender,
            wrapValue(message.value),
            convertHeaders(message),
        )
        kafka.send(record)
        log.trace("sent message as {} to topic: {}", message.sender, topic)
    }

    private fun wrapValue(value: Any?): String? {
        return if (null == value) {
            null
        } else {
            objectMapper.writeValueAsString(value)
        }
    }

    private fun convertHeaders(message: Message<*>): List<RecordHeader> {
        return message.headers.asSequence()
            .map {
                RecordHeader(
                    it.key,
                    it.key.toByteArray(StandardCharsets.UTF_8)
                )
            }
            // add additional headers
            .plus(
                RecordHeader(
                    CustomHeader.ID,
                    message.id.toString().toByteArray(StandardCharsets.UTF_8)
                )
            )
            .plus(
                RecordHeader(
                    CustomHeader.TYPE,
                    message.type.toByteArray(StandardCharsets.UTF_8)
                )
            )
            .plus(
                RecordHeader(
                    CustomHeader.SCHEMA_VERSION,
                    message.version.toString().toByteArray(StandardCharsets.UTF_8)
                )
            )
            .plus(
                RecordHeader(
                    CustomHeader.CONTENT_TYPE,
                    MediaType.APPLICATION_JSON_VALUE.toByteArray(StandardCharsets.UTF_8)
                )
            )
            .toList()
    }

}