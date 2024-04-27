package dev.mbo.serveradmin.messaging.io.messages.heartbeat

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.NoPayload
import dev.mbo.serveradmin.messaging.io.messages.ContentType
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.shared.str.CaseUtil
import java.util.*

class HeartbeatMessage(
    sender: String,
    senderKey: UUID,
    targetKey: UUID
) : Message<NoPayload>(
    sender = sender,
    senderKey = senderKey,
    targetKey = targetKey,
    staticMetadata = staticMetadata
) {

    companion object {
        val staticMetadata = RecordStaticMetadata(
            type = CaseUtil.toTopicName(HeartbeatMessage::class.java.simpleName),
            schemaVersion = "1",
            contentType = ContentType.NONE
        )
    }

}