package dev.mbo.serveradmin.server

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConfigurationProperties("app.server.metadata")
class ServerMetadata {

    lateinit var name: String
    lateinit var key: UUID

}