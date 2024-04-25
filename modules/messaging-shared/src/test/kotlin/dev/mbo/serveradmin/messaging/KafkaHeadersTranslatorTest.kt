package dev.mbo.serveradmin.messaging

import dev.mbo.serveradmin.testutils.RandomUtil
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.kafka.common.header.internals.RecordHeaders
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets

class KafkaHeadersTranslatorTest {

    @Test
    fun headersToMap() {
        // arrange
        val n = RandomUtil.randomInt(5, 15)
        val headers = RecordHeaders(
            Array(n) { i ->
                RecordHeader("k$i", "v$i".toByteArray(StandardCharsets.UTF_8))
            }
        )

        // act
        val actual = KafkaHeadersTranslator.headersToMap(headers)

        // assert
        assertThat(actual).hasSize(n)
        actual.forEach {
            assertThat(it.key).startsWith("k")
            assertThat(it.value).startsWith("v")
        }
    }
}