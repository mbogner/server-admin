package dev.mbo.serveradmin.server

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.SharedTopic
import dev.mbo.serveradmin.messaging.listener.AbstractSingleListener
import dev.mbo.serveradmin.messaging.listener.processor.ProcessorRouter
import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.*

@Component
class ServerListener(
    router: ProcessorRouter,
    val serverMetadata: ServerMetadata,
) : AbstractSingleListener(router) {

    private val log = logger()

    @KafkaListener(topicPattern = SharedTopic.BOARD_TO_GROUND_PATTERN)
    override fun onMessage(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) {
        super.onMessage(record, acknowledgment)
    }

    override fun validateTargetKey(targetKey: UUID?) {
        log.trace("targetKey: {}", targetKey)
        if (null == targetKey || targetKey != serverMetadata.key) {
            throw IllegalArgumentException("Server key mismatch")
        }
    }

    @PostConstruct
    protected fun init() {
        log.info("listening to {}", SharedTopic.BOARD_TO_GROUND_PATTERN)
    }
}
