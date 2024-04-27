package dev.mbo.serveradmin.messaging.listener.processor

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ProcessorRouter(
    processors: List<Processor>,
) {

    private val log = logger()

    private var processorMap: Map<RecordStaticMetadata, Processor> =
        processors.associateBy { it.supportedRecords() }

    fun processorFor(recordStaticMetadata: RecordStaticMetadata): Processor =
        processorMap[recordStaticMetadata]
            ?: throw UnknownMessageTypeException("Unknown processor: $recordStaticMetadata")

    @PostConstruct
    private fun init() {
        processorMap.forEach { log.debug("registered processor: {} -> {}", it.key, it.value.javaClass.name) }
    }

}