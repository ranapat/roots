package org.ranapat.roots.cache

import io.reactivex.rxjava3.core.Maybe
import okhttp3.MediaType
import org.ranapat.roots.Result
import org.ranapat.roots.converter.Converter

fun Maybe<String>.cache(url: String, mediaType: MediaType? = null): Maybe<Result> = flatMap { content ->
    Cache.to(url, content, mediaType)
}

fun <T: Any> Maybe<Result>.instance(valueType: Class<T>): Maybe<T> = map { result ->
    if (result.success) {
        Converter.fromJson(result.contentValue(), valueType)
    } else {
        throw CacheFailedException()
    }
}