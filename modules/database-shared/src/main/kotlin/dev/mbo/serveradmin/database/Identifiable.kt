package dev.mbo.serveradmin.database

import java.io.Serializable

interface Identifiable<T : Serializable> {
    fun getIdentifier(): T?
}