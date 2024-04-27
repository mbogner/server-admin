package dev.mbo.serveradmin.server.command

import dev.mbo.serveradmin.messaging.io.messages.command.Command
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent
class CommandShell(
    private val service: CommandService,
) {

    /**
     * example for sending 2 commands to both clients:
     * <code>
     *     cmd --targets sa-client1,sa-client2 --commands 'echo 1' 'echo 2;echo undo2'
     * </code>
     * both have an up and the second one also defines a down command.
     */
    @ShellMethod(
        key = ["command", "cmd"],
        value = "Trigger a heartbeat message manually"
    )
    fun sendCommands(
        @ShellOption(help = "Target of the commands") targets: String,
        @ShellOption(help = "List of commands to send", arity = -1) commands: List<String>,
    ): String {
        val targetsList = targets.split(",").map { it.trim() }.filterNot { it.isBlank() }
        val commandObjects = commands.map { input ->
            input.split(";").let { Command(up = it[0], down = it.getOrNull(1)) }
        }
        service.sendCommands(targets = targetsList, commands = commandObjects)
        return "sent commands to $targetsList"
    }

}