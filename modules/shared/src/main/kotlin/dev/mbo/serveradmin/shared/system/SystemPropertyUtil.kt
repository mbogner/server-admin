package dev.mbo.serveradmin.shared.system

object SystemPropertyUtil {

    fun readProperties(): Map<String, String?> {
        return System.getProperties()
            .map { it.key.toString() to it.value?.toString() }
            .toMap()
    }

}