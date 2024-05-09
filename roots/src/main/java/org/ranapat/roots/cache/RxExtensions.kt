package org.ranapat.roots.cache

import io.reactivex.rxjava3.core.Maybe
import org.ranapat.roots.converter.Converter
import java.nio.charset.Charset

fun Maybe<String>.cache(url: String, charset: Charset? = null): Maybe<CacheResult> = flatMap { content ->
    Cache.to(url, content, charset)
}

fun <T: Any> Maybe<CacheResult>.instance(valueType: Class<T>): Maybe<T> = map { result ->
    if (result.success) {
        Converter.fromJson(result.content, valueType)
    } else {
        throw CacheFailedException()
    }
}