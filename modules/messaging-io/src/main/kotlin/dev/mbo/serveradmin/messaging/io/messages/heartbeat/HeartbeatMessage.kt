package dev.mbo.serveradmin.messaging.io.messages.heartbeat

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.NoPayload

class HeartbeatMessage(sender: String) : Message<NoPayload>(
    sender = sender,
)