package org.ranapat.roots.entanglement.basic

import io.reactivex.rxjava3.core.Maybe
import org.ranapat.roots.Result
import org.ranapat.roots.api.Get
import org.ranapat.roots.api.result
import org.ranapat.roots.cache.Cache
import org.ranapat.roots.cache.cache
import org.ranapat.roots.converter.instance
import org.ranapat.roots.entanglement.Base
import java.util.Date

class TimedGet<T : Any> : Base<T> {
    private val _flow: Maybe<T>

    constructor(url: String, valueType: Class<T>, ttl: Long): super() {
        val api = Get.from(url)
            .result(Result.Type.TEXT)
            .map { result ->
                result.content!!
            }
            .cache(url)
            .onErrorResumeNext {
                Maybe.empty()
            }
        val cache = Cache
            .from(url)

        _flow = Maybe
            .concat(cache, api)
            .filter { result: Result ->
                result.success && (Date().time - result.lastModified!! <= ttl)
            }
            .concatWith(cache)
            .firstElement()
            .map { result ->
                return@map result.content!!
            }
            .instance(valueType)
    }

    override val flow: Maybe<T>
        get() = _flow
}