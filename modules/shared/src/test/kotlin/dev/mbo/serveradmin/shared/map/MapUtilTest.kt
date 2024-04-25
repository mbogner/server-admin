package dev.mbo.serveradmin.shared.map

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MapUtilTest {

    @Test
    fun readRequiredKey() {
        assertThrows(MapKeyMissingException::class.java) {
            MapUtil.readRequiredKey("test1", mapOf())
        }
        assertThat(MapUtil.readRequiredKey("test2", mapOf("test2" to "t2"))).isEqualTo("t2")
    }
}