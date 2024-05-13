package org.ranapat.roots

import java.nio.charset.Charset

data class Result(
    val type: Type,
    val source: Source,
    val success: Boolean,
    val lastModified: Long?,
    val location: String?,
    val content: String?,
    val encoding: Charset?,
) {
    enum class Type {
        TEXT
    }
    enum class Source {
        API, CACHE
    }
}