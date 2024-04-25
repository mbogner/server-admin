package dev.mbo.serveradmin.shared.str

object CaseUtil {
    private val caseRegex = Regex("[-._\\s\\t]|(?=[A-Z])")

    fun toKebapCase(str: String): String {
        return str
            .trim()
            .split(regex = caseRegex)
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString("-")
            .lowercase()
    }

    fun toTopicName(str: String): String = toKebapCase(str)

}