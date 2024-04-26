package dev.mbo.serveradmin.logging

import dev.mbo.serveradmin.shared.map.MapUtil
import org.slf4j.MDC

object MDCUtil {

    fun addValueAsMdc(value: String, mdcName: String): String {
        MDC.put(mdcName, value)
        return value
    }

    fun readRequiredKeyAndAddToMdc(key: String, data: Map<String, String>, mdcName: String): String {
        return addValueAsMdc(MapUtil.readRequiredKey(key, data), mdcName)
    }

    fun readOptionalKeyAndAddToMdcIfExists(key: String, data: Map<String, String>, mdcName: String): String? {
        val value = data[key]
        if (null != value) {
            MDC.put(mdcName, value)
        }
        return value
    }

}