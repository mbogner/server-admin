package dev.mbo.serveradmin.server.db.client

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.annotations.ColumnTransformer
import java.io.Serializable
import java.time.Instant

@Embeddable
class ClientMetadata(

    @field:Column(name = "metadata_last_request")
    var lastRequest: Instant? = null,

    @field:Column(name = "metadata_ts")
    var ts: Instant? = null,

    @field:Column(name = "metadata_schema")
    var schema: String? = null,

    @field:Column(name = "metadata_schema_version")
    var schemaVersion: String? = null,

    @field:Column(name = "metadata", columnDefinition = "jsonb")
    @field:ColumnTransformer(
        read = "metadata",  // No need to cast on read
        write = "?::jsonb"  // Cast to jsonb on write
    )
    var metadata: String? = null

) : Serializable