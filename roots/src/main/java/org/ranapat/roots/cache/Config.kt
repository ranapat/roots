package org.ranapat.roots.cache

import android.content.Context
import kotlin.String

data class Config(
    val basePath: String,
    val prefix: String,
    val pathStructure: CacheConfig.PathStructure
) {
    constructor(
        context: Context,
        prefix: String? = null,
        pathStructure: CacheConfig.PathStructure? = null
    ) : this(
        context.filesDir.absolutePath,
        prefix ?: CacheConfig.DEFAULT_PREFIX,
        pathStructure ?: CacheConfig.PathStructure.PLAIN
    )
}