package dev.mbo.serveradmin.messaging.io.messages.metadata

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.NoPayload

class MetadataRequest(sender: String) : Message<NoPayload>(
    sender = sender
)