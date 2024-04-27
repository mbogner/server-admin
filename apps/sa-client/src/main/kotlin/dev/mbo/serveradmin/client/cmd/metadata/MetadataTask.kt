package dev.mbo.serveradmin.client.cmd.metadata

import dev.mbo.serveradmin.logging.logger
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class MetadataTask(
    private val service: MetadataService,
    @Value("\${app.task.metadata.send-on-startup:true}")
    private val sendOnStartup: Boolean,
) {

    private val log = logger()

    @PostConstruct
    internal fun init() {
        if (sendOnStartup) {
            log.info("sending metadata on startup")
            service.sendMetadata()
        }
    }

    @Scheduled(fixedRateString = "\${app.task.metadata.seconds}", timeUnit = TimeUnit.SECONDS)
    internal fun resendRegularly() {
        log.info("sending metadata")
        service.sendMetadata()
    }

}