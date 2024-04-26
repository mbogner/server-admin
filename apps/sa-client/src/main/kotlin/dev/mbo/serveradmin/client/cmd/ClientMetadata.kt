package dev.mbo.serveradmin.client.cmd

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConfigurationProperties("app.client.metadata")
class ClientMetadata {

    lateinit var name: String
    lateinit var key: UUID

}