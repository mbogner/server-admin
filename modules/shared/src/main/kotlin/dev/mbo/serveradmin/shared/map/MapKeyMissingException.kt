package dev.mbo.serveradmin.shared.map

class MapKeyMissingException(message: String, val key: String) : RuntimeException(message)