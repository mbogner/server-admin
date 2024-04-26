package dev.mbo.serveradmin.messaging.listener

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.listener.processor.ProcessorRouter
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.support.Acknowledgment
import java.time.Duration

abstract class AbstractBatchListener(
    router: ProcessorRouter
) : AbstractListener(router) {

    private val log = logger()

    open fun onMessage(
        records: List<ConsumerRecord<String, String>>,
        acknowledgment: Acknowledgment
    ) {
        log.trace(
            "received {} entries",
            records.size
        )
        var i = 0
        try {
            for (record in records) {
                process(record)
                i++ // keep track of success index
            }
            log.debug(
                "successfully processed batch of {} entries",
                records.size
            )
            acknowledgment.acknowledge() // all fine
        } catch (exc: Exception) {
            log.debug(
                "failed at index {}",
                i,
                exc
            )
            acknowledgment.nack(
                i,
                Duration.ofMillis(10_000)
            ) // NACK at index i
        }
    }

}