package dev.mbo.serveradmin.messaging.listener

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.listener.processor.ProcessorRouter
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

abstract class AbstractSingleListener(
    router: ProcessorRouter
) : AbstractListener(router) {

    private val log = logger()

    open fun onMessage(
        record: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) {
        try {
            log.trace("received record")
            process(record)
            acknowledgment.acknowledge() // all fine
        } catch (exc: Exception) {
            log.debug("failed on entry", exc)
            acknowledgment.nack(
                Duration.ofMillis(10_000)
            ) // NACK at index i
        }
    }

}