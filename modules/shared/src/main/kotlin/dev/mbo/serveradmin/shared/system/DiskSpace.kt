package dev.mbo.serveradmin.shared.system

import com.fasterxml.jackson.annotation.JsonProperty

data class DiskSpace(
    @field:JsonProperty("total")
    val total: Long,

    @field:JsonProperty("usable")
    val usable: Long,

    @field:JsonProperty("free")
    val free: Long,

    @field:JsonProperty("unit")
    val unit: StorageUnit = StorageUnit.BYTES
)

data class HostData(
    @field:JsonProperty("host_name")
    val hostName: String,

    @field:JsonProperty("host_address")
    val hostAddress: String,

    @field:JsonProperty("canonical_host_name")
    val canonicalHostName: String,
)