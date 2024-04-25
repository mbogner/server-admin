package dev.mbo.serveradmin.client.cmd.heartbeat

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class HeartbeatTask(
    private val heartbeatService: HeartbeatService
) {

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    fun scheduledHeartbeat() {
        heartbeatService.send()
    }

}