package dev.mbo.serveradmin.logging

import dev.mbo.serveradmin.shared.map.MapUtil
import org.slf4j.MDC

object MDCUtil {

    fun readValueAndAddToMdc(value: String, mdcName: String): String {
        MDC.put(mdcName, value)
        return value
    }

    fun readRequiredKeyAndAddToMdc(key: String, data: Map<String, String>, mdcName: String): String {
        return readValueAndAddToMdc(MapUtil.readRequiredKey(key, data), mdcName)
    }

}