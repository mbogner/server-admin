package dev.mbo.serveradmin.messaging.io.messages.heartbeat

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.NoPayload
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
    version = VERSION
) {

    companion object {
        val NAME = CaseUtil.toTopicName(HeartbeatMessage::class.java.simpleName)
        const val VERSION = 1
    }

}