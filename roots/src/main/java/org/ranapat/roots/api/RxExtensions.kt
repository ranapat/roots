package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.ranapat.roots.converter.instance

fun Maybe<Response>.string(): Maybe<String> = map { response ->
    response.body?.string() ?: throw RequestMissingBodyException(
        response.request.url.toString(), Method.GET, response
    )
}
fun Maybe<Response>.jsonObject(): Maybe<JSONObject> = string().map { body ->
    JSONObject(body)
}
fun Maybe<Response>.jsonArray(): Maybe<JSONArray> = string().map { body ->
    JSONArray(body)
}

fun <T: Any> Maybe<Response>.instance(
    valueType: Class<T>
): Maybe<T> = string().instance(valueType)
fun <T: Any> Maybe<Response>.instance(
    normaliseResponse: NormaliseResponse<T>
): Maybe<T> = string().map { body ->
    normaliseResponse.invoke(body)
}