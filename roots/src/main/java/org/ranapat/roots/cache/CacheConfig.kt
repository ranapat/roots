package org.ranapat.roots.cache

object CacheConfig {
    enum class PathStructure {
        PLAIN, NESTED
    }

    const val DEFAULT_PREFIX = "/cache"

    var config: Config? = null
}