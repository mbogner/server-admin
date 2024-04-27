package dev.mbo.serveradmin.server.db.client

import dev.mbo.serveradmin.logging.logger
import dev.mbo.serveradmin.messaging.io.messages.metadata.MetadataRequest
import dev.mbo.serveradmin.messaging.sender.ServerSender
import dev.mbo.serveradmin.server.ServerMetadata
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val serverSender: ServerSender,
    private val serverMetadata: ServerMetadata,
) {

    private val log = logger()

    @Transactional
    fun storeHeartbeat(ts: Instant, sender: String, senderKey: UUID) {
        var client = clientRepository.findByKeyAndName(senderKey, sender)

        // do nothing if we don't know the client's metadata
        if (client == null) {
            client = Client(
                name = sender,
                key = senderKey,
                lastHeartbeat = ts,
            )
            client = clientRepository.save(client)
            log.debug("created client")
        } else if (null == client.lastHeartbeat || ts.isAfter(client.lastHeartbeat)) {
            client.lastHeartbeat = ts
            log.debug("updated client.lastHeartbeat")
        } else {
            log.warn("received outdated heartbeat for {}({})", sender, senderKey)
        }

        requestMetadata(client = client, receiverTopic = sender)
    }

    private fun requestMetadata(client: Client, receiverTopic: String) {
        val now = Instant.now()
        if (null == client.metadata) {
            client.metadata = ClientMetadata()
        }

        if (null == client.metadata?.ts && null == client.metadata!!.lastRequest) {
            log.info("requesting metadata from {}", receiverTopic)
            client.metadata?.lastRequest = now
            sendMetadataRequest(receiverTopic)
        }
    }

    private fun sendMetadataRequest(receiverTopic: String) {
        serverSender.send(
            topic = receiverTopic,
            message = MetadataRequest(
                sender = serverMetadata.name,
                senderKey = serverMetadata.key
            )
        )
    }

    @Transactional
    fun storeMetadata(
        ts: Instant,
        sender: String,
        senderKey: UUID,
        metadata: String,
        schema: String,
        schemaVersion: String
    ) {
        val client = clientRepository.findByKeyAndName(senderKey, sender)
        if (null == client) {
            createMetadata(sender, senderKey, ts, schema, schemaVersion, metadata)
        } else {
            updateMetadata(client, ts, schema, schemaVersion, metadata)
        }
    }

    private fun createMetadata(
        sender: String,
        senderKey: UUID,
        ts: Instant,
        schema: String,
        schemaVersion: String,
        metadata: String
    ) {
        log.debug("create client")
        val client = Client(
            key = senderKey,
            name = sender,
            metadata = ClientMetadata(
                ts = ts,
                schema = schema,
                schemaVersion = schemaVersion,
                metadata = metadata
            )
        )
        clientRepository.save(client)
    }

    private fun updateMetadata(
        client: Client,
        ts: Instant,
        schema: String,
        schemaVersion: String,
        metadata: String
    ) {
        log.debug("update client metadata")
        if (null == client.metadata) {
            client.metadata = ClientMetadata()
        }

        client.metadata?.ts = ts
        client.metadata?.schema = schema
        client.metadata?.schemaVersion = schemaVersion
        client.metadata?.metadata = metadata
        client.metadata?.lastRequest = null
    }

}