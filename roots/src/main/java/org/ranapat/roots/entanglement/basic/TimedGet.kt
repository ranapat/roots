package org.ranapat.roots.entanglement.basic

import io.reactivex.rxjava3.core.Maybe
import okhttp3.MediaType.Companion.toMediaType
import org.ranapat.roots.Result
import org.ranapat.roots.api.Get
import org.ranapat.roots.api.result
import org.ranapat.roots.cache.Cache
import org.ranapat.roots.cache.cache
import org.ranapat.roots.converter.instance
import org.ranapat.roots.entanglement.Base
import org.ranapat.roots.entanglement.EntanglementFailedException
import java.util.Date

class TimedGet<T : Any>(
    url: String, valueType: Class<T>,
    ttl: Long
) : Base<T>() {
    private val _flow: Maybe<T>

    init {
        val api = Get.from(url)
            .result()
            .map { result ->
                result.contentValue<String>()
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
            .doOnEvent { result, _ ->
                if (!result!!.success) {
                    throw EntanglementFailedException()
                }
            }
            .map { result ->
                return@map result.contentValue<String>()
            }
            .instance(valueType)
    }

    override val flow: Maybe<T>
        get() = _flow
}