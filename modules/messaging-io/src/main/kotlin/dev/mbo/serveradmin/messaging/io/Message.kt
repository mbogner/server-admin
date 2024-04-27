package dev.mbo.serveradmin.messaging.io

import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import java.time.Instant
import java.util.*

abstract class Message<T>(
    val id: UUID = UUID.randomUUID(),
    val sender: String,
    val senderKey: UUID,
    val targetKey: UUID? = null,
    val value: T? = null,
    val staticMetadata: RecordStaticMetadata,
    val headers: Map<String, String> = emptyMap(),
    val createdAt: Instant = Instant.now()
) {

    override fun toString(): String {
        return "${javaClass.simpleName}(" +
                "id=$id, " +
                "sender='$sender', " +
                "senderKey=$senderKey, " +
                "targetKey=$targetKey, " +
                "staticMetadata='$staticMetadata', " +
                "headers=$headers, " +
                "createdAt=$createdAt" +
                ")"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Message<*>) return false

        if (id != other.id) return false
        if (sender != other.sender) return false
        if (senderKey != other.senderKey) return false
        if (targetKey != other.targetKey) return false
        if (value != other.value) return false
        if (staticMetadata != other.staticMetadata) return false
        if (headers != other.headers) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + sender.hashCode()
        result = 31 * result + senderKey.hashCode()
        result = 31 * result + (targetKey?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + staticMetadata.hashCode()
        result = 31 * result + headers.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }

}