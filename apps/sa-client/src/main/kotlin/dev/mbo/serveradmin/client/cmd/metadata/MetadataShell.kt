package dev.mbo.serveradmin.client.cmd.metadata

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class MetadataShell(
    private val service: MetadataService,
) {

    @ShellMethod(
        key = ["metadata", "md"],
        value = "Trigger a metadata message manually"
    )
    fun sendMetadata(): String {
        service.sendMetadata()
        return "sent metadata"
    }

}