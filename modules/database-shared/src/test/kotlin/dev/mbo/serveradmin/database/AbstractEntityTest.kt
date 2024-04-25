package dev.mbo.serveradmin.database

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class AbstractEntityTest {

    class Sample(
        val id: Int?
    ) : AbstractEntity<Int>() {
        override fun getIdentifier(): Int? {
            return id
        }
    }

    @ParameterizedTest
    @MethodSource("testSampleSource")
    fun testSample(id: Int?) {
        val actual = Sample(id)
        assertThat(actual.getIdentifier()).isEqualTo(actual.id)
        assertThat(actual.hashCode()).isNotNull()
        assertThat(actual.toString()).startsWith("${Sample::class.java.simpleName}(")
        assertThat(actual.equals(1)).isFalse()
    }

    companion object {
        @JvmStatic
        fun testSampleSource(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(null),
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(Int.MAX_VALUE),
                Arguments.of(Int.MIN_VALUE),
            )
        }
    }
}