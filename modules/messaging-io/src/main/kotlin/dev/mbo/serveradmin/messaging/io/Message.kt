package dev.mbo.serveradmin.messaging.io

import dev.mbo.serveradmin.shared.str.CaseUtil
import java.time.Instant
import java.util.*

abstract class Message<T>(
    val sender: String,
    val id: UUID = UUID.randomUUID(),
    val version: Int = 1,
    val value: T? = null,
    val headers: Map<String, String> = emptyMap(),
    val createdAt: Instant = Instant.now()
) {

    val type: String = CaseUtil.toTopicName(javaClass.simpleName)

    override fun toString(): String {
        return "${javaClass.name}(" +
                "id=$id, " +
                "type=$type, " +
                "sender=$sender, " +
                "version=$version, " +
                "headers=$headers" +
                "createdAt=$createdAt, " +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Message<*>) return false

        if (type != other.type) return false
        if (id != other.id) return false
        if (sender != other.sender) return false
        if (version != other.version) return false
        if (value != other.value) return false
        if (headers != other.headers) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + sender.hashCode()
        result = 31 * result + version
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + headers.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }


}