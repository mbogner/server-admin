package dev.mbo.serveradmin.server.db.client

import dev.mbo.serveradmin.logging.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class ClientService(
    private val clientRepository: ClientRepository,
) {

    private val log = logger()

    @Transactional
    fun storeHeartbeat(ts: Instant, sender: String, senderKey: UUID) {
        val client = clientRepository.findByKeyAndName(senderKey, sender)

        // do nothing if we don't know the client's metadata
        if (client == null) {
            log.warn("received heartbeat from client {}({}) without metadata", sender, senderKey)
            return
        }

        if (null == client.lastHeartbeat || ts.isAfter(client.lastHeartbeat)) {
            client.lastHeartbeat = ts
        }
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
        if (client == null) {
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
        log.debug("update client")
        if (null == client.metadata) {
            client.metadata = ClientMetadata(
                ts = ts,
                schema = schema,
                schemaVersion = schemaVersion,
                metadata = metadata
            )
        } else if (null == client.metadata!!.ts || true == client.metadata!!.ts?.isBefore(ts)) {
            client.metadata!!.ts = ts
            client.metadata!!.schema = schema
            client.metadata!!.schemaVersion = schemaVersion
            client.metadata!!.metadata = metadata
        }
    }

}