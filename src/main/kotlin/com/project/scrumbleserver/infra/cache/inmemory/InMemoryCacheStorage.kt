package com.project.scrumbleserver.infra.cache.inmemory

import com.project.scrumbleserver.infra.cache.CacheStorage
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryCacheStorage : CacheStorage {
    private val storage = ConcurrentHashMap<String, CacheEntry>()

    private data class CacheEntry(
        val value: String,
        private val expiredAt: Long,
    ) {
        companion object {
            fun of(
                value: String,
                expiration: Long,
            ): CacheEntry = CacheEntry(value, System.currentTimeMillis() + expiration)
        }

        val isExpired: Boolean
            get() = System.currentTimeMillis() > expiredAt
    }

    override fun put(
        key: String,
        value: String,
        expiration: Long,
    ) {
        storage[key] = CacheEntry.of(value, expiration)
    }

    override fun get(key: String): String? =
        storage[key]?.let {
            if (it.isExpired) {
                storage.remove(key)
                null
            } else {
                it.value
            }
        }

    override fun remove(key: String) {
        storage.remove(key)
    }
}
