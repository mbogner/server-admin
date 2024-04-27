package dev.mbo.serveradmin.messaging.io.messages.command

import dev.mbo.serveradmin.messaging.io.Message
import dev.mbo.serveradmin.messaging.io.messages.ContentType
import dev.mbo.serveradmin.messaging.io.messages.RecordStaticMetadata
import dev.mbo.serveradmin.shared.str.CaseUtil
import java.util.*

class CommandMessage(
    sender: String,
    senderKey: UUID,
    value: CommandPayload
) : Message<CommandPayload>(
    sender = sender,
    senderKey = senderKey,
    value = value,
    staticMetadata = staticMetadata
) {

    companion object {
        val staticMetadata = RecordStaticMetadata(
            type = CaseUtil.toTopicName(CommandMessage::class.java.simpleName),
            schemaVersion = "1",
            contentType = ContentType.JSON
        )
    }

}