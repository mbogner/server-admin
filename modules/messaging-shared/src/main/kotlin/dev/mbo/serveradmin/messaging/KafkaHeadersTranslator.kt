package dev.mbo.serveradmin.messaging

import org.apache.kafka.common.header.Headers
import java.nio.charset.StandardCharsets

object KafkaHeadersTranslator {
    fun headersToMap(headers: Headers): Map<String, String> {
        return headers.associate { header ->
            header.key() to String(header.value(), StandardCharsets.UTF_8)
        }
    }
}