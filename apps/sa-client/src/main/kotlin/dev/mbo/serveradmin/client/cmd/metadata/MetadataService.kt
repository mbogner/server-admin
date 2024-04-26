package dev.mbo.serveradmin.client.cmd.metadata

import dev.mbo.serveradmin.client.cmd.ClientMetadata
import dev.mbo.serveradmin.client.cmd.ServerMetadata
import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataMessage
import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataPayload
import dev.mbo.serveradmin.messaging.sender.ClientSender
import dev.mbo.serveradmin.shared.system.DiskSpaceUtil
import dev.mbo.serveradmin.shared.system.HostDataUtil
import dev.mbo.serveradmin.shared.system.SystemEnvUtil
import dev.mbo.serveradmin.shared.system.SystemPropertyUtil
import org.springframework.stereotype.Service

@Service
class MetadataService(
    private val clientMetadata: ClientMetadata,
    private val serverMetadata: ServerMetadata,
    private val sender: ClientSender,
) {

    private val log = logger()

    fun send() {
        val metadata = MetadataPayload(
            hostData = HostDataUtil.readHostData(),
            diskSpace = DiskSpaceUtil.readDiskSpace(),
            env = SystemEnvUtil.readEnv(),
            systemProperties = SystemPropertyUtil.readProperties(),
        )
        log.debug("metadata = {}", metadata)
        sender.boardToGround(
            MetadataMessage(
                sender = clientMetadata.name,
                senderKey = clientMetadata.key,
                targetKey = serverMetadata.key,
                value = metadata
            )
        )
    }

}