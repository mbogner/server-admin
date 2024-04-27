package dev.mbo.serveradmin.client.cmd.heartbeat

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class HeartbeatShell(
    private val heartbeatService: HeartbeatService,
) {

    @ShellMethod(
        key = ["heartbeat", "hb"],
        value = "Trigger a heartbeat message manually"
    )
    fun sendHeartbeat(): String {
        heartbeatService.sendHeartbeat()
        return "sent heartbeat"
    }

}