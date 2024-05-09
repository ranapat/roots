package org.ranapat.roots.cache

import java.io.File
import java.net.URI

abstract class Base {
    @Throws(DefaultConfigNotSetException::class)
    protected fun ensureConfig(): Config {
        if (CacheConfig.config == null) {
            throw DefaultConfigNotSetException()
        }

        return CacheConfig.config!!
    }

    protected fun ensurePath(path: String): File? = File(path).let {
        if (it.exists()) {
            return@let it
        } else if (it.mkdirs()) {
            return@let it
        } else {
            return null
        }
    }

    protected fun ensureCacheFile(url: String): File? {
        val config = ensureConfig()

        val cachePath: String
        val cacheName: String

        if (config.pathStructure == CacheConfig.PathStructure.NESTED) {
            val uriPathParts = URI(
                url.replace(" ".toRegex(), "_")
            ).path.split("/")
            cachePath = config.basePath + config.prefix + uriPathParts.subList(0, uriPathParts.size - 1).joinToString("/")
            cacheName = uriPathParts.last()
        } else {
            cachePath = config.basePath + config.prefix
            cacheName = URI(
                url.replace(" ".toRegex(), "_")
            ).path.replace("/".toRegex(), "_")
        }

        val path = ensurePath(cachePath)
        return if (path != null) {
            File(path, cacheName)
        } else {
            null
        }
    }
}