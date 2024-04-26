package dev.mbo.serveradmin.shared.system

import java.net.InetAddress

object HostDataUtil {

    fun readHostData(): HostData {
        val localhost = InetAddress.getLocalHost()
        return HostData(
            hostName = localhost.hostName,
            hostAddress = localhost.hostAddress,
            canonicalHostName = localhost.canonicalHostName,
        )
    }

}