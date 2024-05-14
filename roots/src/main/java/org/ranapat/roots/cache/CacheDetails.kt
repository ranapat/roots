package org.ranapat.roots.cache

import android.content.Context

object CacheDetails {
    enum class PathStructure {
        PLAIN, NESTED
    }

    data class Config(
        val basePath: String,
        val prefix: String,
        val pathStructure: CacheDetails.PathStructure
    ) {
        constructor(
            context: Context,
            prefix: String? = null,
            pathStructure: CacheDetails.PathStructure? = null
        ) : this(
            context.filesDir.absolutePath,
            prefix ?: CacheDetails.DEFAULT_PREFIX,
            pathStructure ?: CacheDetails.PathStructure.PLAIN
        )
    }

    const val DEFAULT_PREFIX = "/cache"

    var config: Config? = null
}