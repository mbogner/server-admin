package dev.mbo.serveradmin.shared.str

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CaseUtilTest {

    @Test
    fun toKebapCase() {
        assertThat(CaseUtil.toKebapCase(" this.is  a_TestString. ")).isEqualTo("this-is-a-test-string")
    }
}