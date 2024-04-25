package dev.mbo.serveradmin.testutils

import java.security.SecureRandom

object RandomUtil {

    private val random = SecureRandom.getInstanceStrong()

    fun randomInt(from: Int = 0, until: Int = Int.MAX_VALUE): Int {
        return random.nextInt(from, until)
    }



}