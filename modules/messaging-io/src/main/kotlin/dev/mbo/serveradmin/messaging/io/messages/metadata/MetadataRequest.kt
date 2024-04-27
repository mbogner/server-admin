package dev.mbo.serveradmin.messaging.io.messages.metadata

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.NoPayload
import dev.mbo.serveradmin.messaging.io.messages.ContentType
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.shared.str.CaseUtil
import java.util.*

class MetadataRequest(
    sender: String,
    senderKey: UUID,
) : Message<NoPayload>(
    sender = sender,
    senderKey = senderKey,
    staticMetadata = staticMetadata
) {

    companion object {
        val staticMetadata = RecordStaticMetadata(
            type = CaseUtil.toTopicName(MetadataRequest::class.java.simpleName),
            schemaVersion = "1",
            contentType = ContentType.NONE
        )
    }

}