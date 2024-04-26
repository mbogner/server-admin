package dev.mbo.serveradmin.messaging.listener

import dev.mbo.serveradmin.logging.MDCUtil
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.KafkaHeadersTranslator
import dev.mbo.serveradmin.messaging.listener.processor.HeaderMissingException
import dev.mbo.serveradmin.messaging.listener.processor.Processor
import dev.mbo.serveradmin.messaging.listener.processor.ProcessorRouter
import dev.mbo.serveradmin.messaging.sender.CustomHeader
import dev.mbo.serveradmin.shared.map.MapKeyMissingException
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.MDC
import java.time.Instant
import java.util.*

abstract class AbstractListener(
    private val router: ProcessorRouter
) {

    private val log = logger()

    protected open fun validateSenderKey(senderKey: UUID) {}
    protected open fun validateTargetKey(targetKey: UUID?) {}

    protected open fun process(record: ConsumerRecord<String, String>) {
        // parse headers
        val headersRaw = KafkaHeadersTranslator.headersToMap(record.headers())

        try {
            // read required values
            val id = MDCUtil.readRequiredKeyAndAddToMdc(CustomHeader.ID, headersRaw, MDC_RECORD_ID)
            val recordStaticMetadata = Processor.RecordStaticMetadata(
                type = MDCUtil.readRequiredKeyAndAddToMdc(CustomHeader.TYPE, headersRaw, MDC_RECORD_TYPE),
                schemaVersion = MDCUtil.readRequiredKeyAndAddToMdc(
                    CustomHeader.SCHEMA_VERSION,
                    headersRaw,
                    MDC_RECORD_SCHEMA_VERSION
                ),
                contentType = MDCUtil.readRequiredKeyAndAddToMdc(
                    CustomHeader.CONTENT_TYPE,
                    headersRaw,
                    MDC_RECORD_CONTENT_TYPE
                )
            )
            val topic = MDCUtil.addValueAsMdc(record.topic(), MDC_RECORD_TOPIC)
            val sender = MDCUtil.addValueAsMdc(record.key(), MDC_RECORD_SENDER)
            val senderKey = UUID.fromString(
                MDCUtil.readRequiredKeyAndAddToMdc(CustomHeader.SENDER_KEY, headersRaw, MDC_RECORD_SENDER_KEY)
            )
            validateSenderKey(senderKey)
            val targetKeyStr = MDCUtil.readOptionalKeyAndAddToMdcIfExists(
                CustomHeader.TARGET_KEY, headersRaw, MDC_RECORD_TARGET_KEY
            )
            val targetKey = if (null == targetKeyStr) {
                null
            } else {
                val parsed = UUID.fromString(targetKeyStr)
                validateTargetKey(parsed)
                parsed
            }
            val ts = Instant.ofEpochMilli(record.timestamp())
            MDCUtil.addValueAsMdc(ts.toString(), MDC_RECORD_TS)

            log.trace("read metadata")

            // get processor for message
            val processor = router.processorFor(
                recordStaticMetadata = recordStaticMetadata
            )

            // process
            log.debug("using processor {}", processor::class.java.name)
            processor.process(
                value = record.value(),
                headersRaw = headersRaw,
                recordId = id,
                recordMetadata = Processor.RecordMetadata(
                    topic = topic,
                    ts = ts,
                    sender = sender,
                    senderKey = senderKey,
                    targetKey = targetKey,
                ),
                recordStaticMetadata = recordStaticMetadata
            )
            log.debug("successfully processed record")
        } catch (exc: MapKeyMissingException) {
            throw HeaderMissingException(exc.message!!, header = exc.key, exc)
        } finally {
            cleanup()
        }
    }

    private fun cleanup() {
        MDC_RECORD_ALL.forEach { MDC.remove(it) }
    }

    companion object {
        const val MDC_RECORD_SENDER = "record.sender"
        const val MDC_RECORD_TOPIC = "record.topic"
        const val MDC_RECORD_ID = "record.id"
        const val MDC_RECORD_TYPE = "record.type"
        const val MDC_RECORD_SCHEMA_VERSION = "record.schemaVersion"
        const val MDC_RECORD_CONTENT_TYPE = "record.contentType"
        const val MDC_RECORD_SENDER_KEY = "record.senderKey"
        const val MDC_RECORD_TARGET_KEY = "record.targetKey"
        const val MDC_RECORD_TS = "record.ts"

        val MDC_RECORD_ALL = setOf(
            MDC_RECORD_SENDER,
            MDC_RECORD_TOPIC,
            MDC_RECORD_ID,
            MDC_RECORD_TYPE,
            MDC_RECORD_SCHEMA_VERSION,
            MDC_RECORD_CONTENT_TYPE,
            MDC_RECORD_SENDER_KEY,
            MDC_RECORD_TARGET_KEY,
            MDC_RECORD_TS
        )
    }
}