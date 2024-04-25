package dev.mbo.serveradmin.messaging.processor

import org.springframework.stereotype.Component

@Component
class ProcessorRouter(
    processors: List<Processor>,
) {

    private var processorMap: Map<Processor.RecordStaticMetadata, Processor> =
        processors.associateBy { it.supportedRecords() }

    fun processorFor(recordStaticMetadata: Processor.RecordStaticMetadata): Processor =
        processorMap[recordStaticMetadata] ?: throw UnknownMessageTypeException("Unknown processor: $recordStaticMetadata")

}