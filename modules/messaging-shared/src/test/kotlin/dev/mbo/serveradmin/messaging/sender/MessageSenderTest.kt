package dev.mbo.serveradmin.messaging.sender

import dev.mbo.serveradmin.messaging.TestContext
import dev.mbo.serveradmin.messaging.io.messages.heartbeat.HeartbeatMessage
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.ContextConfiguration
import java.util.*

@SpringBootTest
@ContextConfiguration(classes = [TestContext::class])
class MessageSenderTest {

    @MockBean
    private lateinit var kafka: KafkaTemplate<String, String>

    @Autowired
    private lateinit var sender: MessageSender

    @Test
    fun broadcast() {
        // arrange
        val msg = createTestMessage()

        // act
        sender.broadcast(msg)

        // assert
        verifyKafkaSends()
    }

    @Test
    fun boardToGround() {
        // arrange
        val msg = createTestMessage()

        // act
        sender.boardToGround(msg)

        // assert
        verifyKafkaSends()
    }

    @Test
    fun send() {
        // arrange
        val topic = "target"
        val msg = createTestMessage()

        // act
        sender.send(topic, msg)

        // assert
        verifyKafkaSends()
    }

    private fun createTestMessage(): HeartbeatMessage {
        return HeartbeatMessage(
            sender = "test",
            senderKey = UUID.randomUUID(),
            targetKey = UUID.randomUUID()
        )
    }

    private fun verifyKafkaSends() {
        verify(kafka, times(1)).send(any<ProducerRecord<String, String>>())
    }
}