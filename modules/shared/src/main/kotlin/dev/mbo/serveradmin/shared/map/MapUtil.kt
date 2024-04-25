package dev.mbo.serveradmin.shared.map

object MapUtil {

    fun readRequiredKey(key: String, data: Map<String, String>): String {
        return data[key]
            ?: throw MapKeyMissingException("required key $key missing", key)
    }

}