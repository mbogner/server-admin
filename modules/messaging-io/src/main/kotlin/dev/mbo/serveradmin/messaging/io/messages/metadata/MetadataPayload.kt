package dev.mbo.serveradmin.messaging.io.messages.metadata

import com.fasterxml.jackson.annotation.JsonProperty
import dev.mbo.serveradmin.shared.system.DiskSpace
import dev.mbo.serveradmin.shared.system.HostData

data class MetadataPayload(
    @field:JsonProperty("host_data")
    val hostData: HostData? = null,

    @field:JsonProperty("disk_space")
    val diskSpace: Map<String, DiskSpace> = emptyMap(),

    @field:JsonProperty("env")
    val env: Map<String, String> = emptyMap(),

    @field:JsonProperty("system_properties")
    val systemProperties: Map<String, String?> = emptyMap(),
)
