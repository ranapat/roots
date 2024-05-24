package org.ranapat.roots

import okhttp3.MediaType

data class Result(
    val source: Source,
    val success: Boolean,
    val lastModified: Long?,
    val location: String?,
    val mediaType: MediaType?,
    val content: Any?,
) {
    enum class Source {
        API, CACHE
    }
    class TypeNotImplementedException
        : IllegalStateException("Type not implemented")

    @Suppress("UNCHECKED_CAST")
    fun <T> contentOrNull(): T? = content as? T

    @Suppress("UNCHECKED_CAST")
    fun <T> contentValue(): T = content as T
}