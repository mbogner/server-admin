package dev.mbo.serveradmin.server.db.client

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ClientRepository : JpaRepository<Client, Int> {

    fun findByKeyAndName(key: UUID, name: String): Client?

}