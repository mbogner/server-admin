package dev.mbo.serveradmin.database

import java.io.Serializable

interface Identifiable<T : Serializable> : Serializable {
    fun getIdentifier(): T?
}