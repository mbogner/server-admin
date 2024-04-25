package dev.mbo.serveradmin.logging

import org.junit.jupiter.api.Test

class LoggerTest {

    private val log = logger()

    @Test
    fun test() {
        log.info("this is a test")
    }

}