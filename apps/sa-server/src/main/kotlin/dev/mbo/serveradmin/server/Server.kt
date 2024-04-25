package dev.mbo.serveradmin.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import java.util.*

@ComponentScan(basePackages = ["dev.mbo.serveradmin"])
@SpringBootApplication
class Server

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    runApplication<Server>(*args)
}