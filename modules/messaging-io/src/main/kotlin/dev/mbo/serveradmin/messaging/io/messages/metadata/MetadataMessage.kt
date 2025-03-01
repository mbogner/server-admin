package dev.mbo.serveradmin.messaging.io.messages.metadata

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.messages.ContentType
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.shared.str.CaseUtil
import java.util.*

class MetadataMessage(
    sender: String,
    senderKey: UUID,
    targetKey: UUID,
    value: MetadataPayload
) : Message<MetadataPayload>(
    sender = sender,
    senderKey = senderKey,
    targetKey = targetKey,
    value = value,
    staticMetadata = staticMetadata
) {

    companion object {
        val staticMetadata = RecordStaticMetadata(
            type = CaseUtil.toTopicName(MetadataMessage::class.java.simpleName),
            schemaVersion = "1",
            contentType = ContentType.JSON
        )
    }

}