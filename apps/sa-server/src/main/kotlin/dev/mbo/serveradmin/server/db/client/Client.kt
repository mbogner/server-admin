package dev.mbo.serveradmin.server.db.client

import dev.mbo.serveradmin.database.AbstractVersionedEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.*

@Entity
@Table(name = "clients")
class Client(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @field:NotNull
    @field:Column(name = "key", nullable = false)
    var key: UUID? = null,

    @field:NotBlank
    @field:Column(name = "name", nullable = false)
    var name: String? = null,

    @field:Column(name = "last_heartbeat")
    var lastHeartbeat: Instant? = null,

    @field:Valid
    @field:Embedded
    var metadata: ClientMetadata? = null

) : AbstractVersionedEntity<Int>() {

    override fun getIdentifier(): Int? {
        return id
    }

}