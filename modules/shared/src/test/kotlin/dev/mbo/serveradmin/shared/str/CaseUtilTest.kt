package dev.mbo.serveradmin.shared.str

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CaseUtilTest {

    @Test
    fun toKebapCase() {
        assertThat(CaseUtil.toKebapCase(" this.is  a_TestString. ")).isEqualTo("this-is-a-test-string")
    }
}