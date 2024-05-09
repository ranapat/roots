package org.ranapat.roots.cache

import io.reactivex.rxjava3.core.Maybe
import org.ranapat.roots.converter.Converter
import java.nio.charset.Charset

fun Maybe<String>.cache(url: String, charset: Charset? = Charsets.UTF_8): Maybe<Result> = flatMap { content ->
    Cache.to(url, content, charset)
}

fun <T: Any> Maybe<Result>.instance(valueType: Class<T>): Maybe<T> = map { result ->
    if (result.success) {
        Converter.fromJson(result.content, valueType)
    } else {
        throw CacheFailedException()
    }
}