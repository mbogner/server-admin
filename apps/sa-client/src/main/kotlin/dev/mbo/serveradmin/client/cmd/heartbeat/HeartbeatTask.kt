package dev.mbo.serveradmin.client.cmd.heartbeat

import dev.mbo.serveradmin.logging.logger
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HeartbeatTask(
    private val service: HeartbeatService,
    @Value("\${app.task.heartbeat.send-on-startup:true}")
    private val sendOnStartup: Boolean,
) {

    private val log = logger()

    @PostConstruct
    internal fun init() {
        if (sendOnStartup) {
            log.debug("sending heartbeat on startup")
            service.sendHeartbeat()
        }
    }

    @Scheduled(fixedRateString = "\${app.task.heartbeat.seconds}", timeUnit = TimeUnit.SECONDS)
    fun scheduledHeartbeat() {
        log.debug("sending heartbeat")
        service.sendHeartbeat()
    }

}