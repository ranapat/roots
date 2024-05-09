package org.ranapat.roots.cache

import kotlin.String

object CacheConfig {
    enum class PathStructure(val value: String) {
        PLAIN("plain"),
        NESTED("nested")
    }

    const val DEFAULT_PREFIX = "/cache"

    var config: Config? = null
}