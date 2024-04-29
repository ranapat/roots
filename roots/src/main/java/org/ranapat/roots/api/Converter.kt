package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import okhttp3.Response
import org.ranapat.roots.api.Base.toTyped

fun <T : Any> Maybe<Response>.like(
    valueType: Class<T>,
    normaliseResponse: NormaliseResponse<T>? = null
): Maybe<T> = map {
    toTyped(it, valueType, normaliseResponse)
}