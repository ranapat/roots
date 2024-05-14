package org.ranapat.roots.cache

import java.io.File
import java.net.URI

abstract class Base {
    @Throws(DefaultConfigNotSetException::class)
    protected fun ensureConfig(): CacheDetails.Config {
        if (CacheDetails.config == null) {
            throw DefaultConfigNotSetException()
        }

        return CacheDetails.config!!
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

    protected fun ensureCacheFileForWriting(url: String): File? {
        val config = ensureConfig()

        val cachePath: String
        val cacheName: String

        if (config.pathStructure == CacheDetails.PathStructure.NESTED) {
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

    protected fun ensureCacheFileForReading(url: String): File? {
        val file = ensureCacheFileForWriting(url)
        return if (file != null && file.exists()) {
            file
        } else {
            null
        }
    }
}