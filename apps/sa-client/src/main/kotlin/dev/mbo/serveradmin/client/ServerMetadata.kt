package dev.mbo.serveradmin.client

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
@ConfigurationProperties("app.server")
class ServerMetadata {

    lateinit var key: UUID

}