package dev.mbo.serveradmin.shared.system

import java.io.File

object DiskSpaceUtil {

    fun readDiskSpace(): Map<String, DiskSpace> {
        return File.listRoots().associate {
            it.absolutePath to DiskSpace(
                free = it.freeSpace,
                total = it.totalSpace,
                usable = it.usableSpace,
            )
        }
    }

}