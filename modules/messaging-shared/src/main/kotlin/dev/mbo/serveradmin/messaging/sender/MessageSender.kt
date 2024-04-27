package dev.mbo.serveradmin.messaging.sender

import com.fasterxml.jackson.databind.ObjectMapper
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.SharedTopic
import dev.mbo.serveradmin.messaging.io.Message
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeader
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
        send("${SharedTopic.BOARD_TO_GROUND}.${message.staticMetadata.type}", message)
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
        var resultMap = message.headers.asSequence()
            .map {
                RecordHeader(
                    it.key,
                    it.value.toByteArray(StandardCharsets.UTF_8)
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
                    message.staticMetadata.type.toByteArray(StandardCharsets.UTF_8)
                )
            )
            .plus(
                RecordHeader(
                    CustomHeader.SCHEMA_VERSION,
                    message.staticMetadata.schemaVersion.toByteArray(StandardCharsets.UTF_8)
                )
            )
            .plus(
                RecordHeader(
                    CustomHeader.CONTENT_TYPE,
                    message.staticMetadata.contentType.toByteArray(StandardCharsets.UTF_8)
                )
            )
            .plus(
                RecordHeader(
                    CustomHeader.SENDER_KEY,
                    message.senderKey.toString().toByteArray(StandardCharsets.UTF_8)
                )
            )
        if (message.targetKey != null) {
            resultMap = resultMap.plus(
                RecordHeader(
                    CustomHeader.TARGET_KEY,
                    message.targetKey.toString().toByteArray(StandardCharsets.UTF_8)
                )
            )
        }
        return resultMap.toList()
    }

}