package dev.mbo.serveradmin.messaging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SharedTopicTest {

    @Test
    fun testAccess() {
        assertThat(SharedTopic.BOARD_TO_GROUND_PATTERN).isNotBlank()
        assertThat(SharedTopic.CLIENT_BROADCAST).isNotBlank()
    }

}