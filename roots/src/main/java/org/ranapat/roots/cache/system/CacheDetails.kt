package org.ranapat.roots.cache.system

import android.content.Context

object CacheDetails {
    const val DEFAULT_PREFIX = "/cache"

    enum class PathStructure {
        PLAIN, NESTED
    }

    data class Config(
        val basePath: String,
        val prefix: String,
        val pathStructure: PathStructure
    ) {
        constructor(
            context: Context,
            prefix: String? = null,
            pathStructure: PathStructure? = null
        ) : this(
            context.filesDir.absolutePath,
            prefix ?: DEFAULT_PREFIX,
            pathStructure ?: PathStructure.PLAIN
        )
    }

    var config: Config? = null
}