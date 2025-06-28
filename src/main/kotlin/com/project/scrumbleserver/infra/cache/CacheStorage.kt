package com.project.scrumbleserver.infra.cache

interface CacheStorage {
    companion object {
        private const val ONE_DAY = 3600L * 24L
    }

    fun put(key: String, value: String, expiration: Long = ONE_DAY)

    fun get(key: String): String?

    fun remove(key: String)
}