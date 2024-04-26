package dev.mbo.serveradmin.shared.system

object SystemEnvUtil {

    fun readEnv(): Map<String, String> {
        return System.getenv()
            // get rid of secrets
            .filterNot {
                val key = it.key.lowercase()
                key.contains("pass") ||
                        key.contains("secret") ||
                        key.contains("key") ||
                        key.contains("token")
            }
    }

}