package dev.mbo.serveradmin.messaging

/**
 * Share topic names for sender and receiver.
 *
 * This is an object instead of an enum so that it can be used in @KafkaListener directly.
 */
object SharedTopic {
    const val BOARD_TO_GROUND = "sa-board-to-ground"
    const val BOARD_TO_GROUND_PATTERN = "${BOARD_TO_GROUND}\\..*"
    const val CLIENT_BROADCAST = "sa-client-broadcast"
}