package dev.mbo.serveradmin.database

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import jakarta.persistence.Version
import java.io.Serializable
import java.time.Instant

@MappedSuperclass
abstract class AbstractVersionedEntity<T : Serializable>(

    @field:Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @field:Version
    @field:Column(name = "lock_version", nullable = false)
    var lockVersion: Int? = null

) : AbstractEntity<T>() {

    override fun toStringMap(): Map<String, Any?> {
        return super.toStringMap()
            .plus("updatedAt" to updatedAt)
            .plus("lockVersion" to lockVersion)
    }

    @PreUpdate
    protected fun preUpdate() {
        updatedAt = Instant.now()
    }

}