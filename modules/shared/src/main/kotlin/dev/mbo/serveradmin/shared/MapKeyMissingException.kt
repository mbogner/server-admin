package dev.mbo.serveradmin.shared

class MapKeyMissingException(message: String, val key: String) : RuntimeException(message)