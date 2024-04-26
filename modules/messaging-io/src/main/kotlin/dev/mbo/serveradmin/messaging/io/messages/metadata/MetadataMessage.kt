package dev.mbo.serveradmin.messaging.io.messages.metadata

import dev.mbo.serveradmin.messaging.io.Message
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
    version = VERSION
) {

    companion object {
        val NAME = CaseUtil.toTopicName(MetadataMessage::class.java.simpleName)
        const val VERSION = 1
    }

}