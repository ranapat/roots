package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import okhttp3.Response
import org.ranapat.roots.converter.instance

fun Maybe<Response>.body(): Maybe<String> = map { response ->
    response.body?.string() ?: throw RequestMissingBodyException(
        response.request.url.toString(), Method.GET, response
    )
}

fun <T: Any> Maybe<Response>.instance(valueType: Class<T>): Maybe<T> = body().instance(valueType)