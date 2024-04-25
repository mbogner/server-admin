package dev.mbo.serveradmin.messaging.listener

import dev.mbo.serveradmin.logging.MDCUtil
import dev.mbo.serveradmin.shared.MapKeyMissingException
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.KafkaHeadersTranslator
import dev.mbo.serveradmin.messaging.processor.HeaderMissingException
import dev.mbo.serveradmin.messaging.processor.Processor
import dev.mbo.serveradmin.messaging.processor.ProcessorRouter
import dev.mbo.serveradmin.messaging.sender.CustomHeader
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.MDC

abstract class AbstractListener(
    private val router: ProcessorRouter
) {

    private val log = logger()

    protected open fun process(record: ConsumerRecord<String, String>) {
        // parse headers
        val headers = KafkaHeadersTranslator.headersToMap(record.headers())

        try {
            // read required values
            val id = MDCUtil.readRequiredKeyAndAddToMdc(CustomHeader.ID, headers, MDC_RECORD_ID)
            val recordStaticMetadata = Processor.RecordStaticMetadata(
                type = MDCUtil.readRequiredKeyAndAddToMdc(CustomHeader.TYPE, headers, MDC_RECORD_TYPE),
                schemaVersion = MDCUtil.readRequiredKeyAndAddToMdc(
                    CustomHeader.SCHEMA_VERSION,
                    headers,
                    MDC_RECORD_SCHEMA_VERSION
                ),
                contentType = MDCUtil.readRequiredKeyAndAddToMdc(
                    CustomHeader.CONTENT_TYPE,
                    headers,
                    MDC_RECORD_CONTENT_TYPE
                )
            )
            val topic = MDCUtil.readValueAndAddToMdc(record.topic(), MDC_RECORD_TOPIC)
            val sender = MDCUtil.readValueAndAddToMdc(record.key(), MDC_RECORD_SENDER)

            // get processor for message
            val processor = router.processorFor(
                recordStaticMetadata = recordStaticMetadata
            )

            // process
            log.debug("received record, using processor {}", processor::class.java.name)
            processor.process(
                topic = topic,
                sender = sender,
                value = record.value(),
                headers = headers,
                recordId = id,
                recordStaticMetadata = recordStaticMetadata
            )

        } catch (exc: MapKeyMissingException) {
            throw HeaderMissingException(exc.message!!, header = exc.key, exc)
        } finally {
            cleanup()
        }
    }

    private fun cleanup() {
        MDC.remove(MDC_RECORD_SENDER)
        MDC.remove(MDC_RECORD_TOPIC)
        MDC.remove(MDC_RECORD_ID)
        MDC.remove(MDC_RECORD_TYPE)
        MDC.remove(MDC_RECORD_SCHEMA_VERSION)
        MDC.remove(MDC_RECORD_CONTENT_TYPE)
    }

    companion object {
        const val MDC_RECORD_SENDER = "record.sender"
        const val MDC_RECORD_TOPIC = "record.topic"
        const val MDC_RECORD_ID = "record.id"
        const val MDC_RECORD_TYPE = "record.type"
        const val MDC_RECORD_SCHEMA_VERSION = "record.schemaVersion"
        const val MDC_RECORD_CONTENT_TYPE = "record.contentType"
    }
}