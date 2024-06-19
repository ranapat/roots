package org.ranapat.roots.cache.system

import io.reactivex.rxjava3.core.Maybe
import okhttp3.MediaType
import org.ranapat.roots.Result

fun Maybe<String>.cache(url: String, mediaType: MediaType? = null): Maybe<Result> = flatMap { content ->
    Cache.to(url, content, mediaType)
}