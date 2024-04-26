package dev.mbo.serveradmin.database

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.io.Serializable
import java.time.Instant

@MappedSuperclass
abstract class AbstractEntity<T : Serializable>(

    @field:Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()

) : Identifiable<T> {

    override fun toString(): String {
        return "${javaClass.simpleName}(${toStringMap().toSortedMap()})"
    }

    protected open fun toStringMap(): Map<String, Any?> {
        return mapOf(
            "id" to getIdentifier(),
            "createdAt" to createdAt,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractEntity<*>) return false
        if (getIdentifier() != other.getIdentifier()) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}