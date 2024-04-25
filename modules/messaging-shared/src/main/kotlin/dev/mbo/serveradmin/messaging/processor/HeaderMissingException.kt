package dev.mbo.serveradmin.messaging.processor

class HeaderMissingException(message: String?, val header: String, exc: Throwable?) : RuntimeException(message, exc)