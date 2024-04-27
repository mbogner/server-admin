package dev.mbo.serveradmin.client

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.listener.AbstractSingleListener
import dev.mbo.serveradmin.messaging.listener.processor.ProcessorRouter
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClientListener(
    @Value("\${app.kafka.topics}")
    private val kafkaTopics: String,
    private val serverMetadata: ServerMetadata,
    router: ProcessorRouter
) : AbstractSingleListener(router) {

    private val log = logger()

    @KafkaListener(topics = ["#{'\${app.kafka.topics}'.split(',')}"])
    override fun onMessage(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) {
        super.onMessage(record, acknowledgment)
    }

    /**
     * Check if the received message is coming from the server.
     */
    override fun validateSenderKey(senderKey: UUID) {
        log.trace("senderKey: {}", senderKey)
        if (senderKey != serverMetadata.key) {
            throw IllegalArgumentException("Server key doesn't match server key")
        }
    }

    @PostConstruct
    protected fun init() {
        log.info("listening to {}", kafkaTopics)
    }
}
