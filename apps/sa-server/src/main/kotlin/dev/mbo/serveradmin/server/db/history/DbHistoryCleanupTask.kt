package dev.mbo.serveradmin.server.db.history

import dev.mbo.serveradmin.logging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class DbHistoryCleanupTask(
    private val jdbc: JdbcTemplate,
    @Value("\${app.task.db-history-cleanup.cutoff}")
    private val cutoffSeconds: Long
) {

    private val log = logger()

    @Scheduled(cron = "\${app.task.db-history-cleanup.cron}")
    internal fun cleanup() {
        val cutoffTime = Timestamp.from(Instant.now().minusSeconds(cutoffSeconds))
        log.info("cleaning up db_history table for records older than {} seconds = {}", cutoffSeconds, cutoffTime)
        val rowsAffected = jdbc.update("DELETE FROM db_history WHERE created_at < ?", cutoffTime)
        log.info("deleted {} row(s) from db_history", rowsAffected)
    }

}