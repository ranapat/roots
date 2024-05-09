package org.ranapat.roots.cache

import java.nio.charset.Charset

data class CacheResult(
    val success: Boolean,
    val location: String?,
    val content: String,
    val encoding: Charset
)