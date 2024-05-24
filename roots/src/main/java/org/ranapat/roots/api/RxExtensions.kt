package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.ranapat.roots.Result
import org.ranapat.roots.converter.instance
import org.ranapat.roots.tools.isApplicationJson
import java.util.Date

fun Maybe<Response>.result(): Maybe<Result> = map { response ->
    val mediaType = response.headers["Content-Type"]?.toMediaTypeOrNull()

    if (mediaType?.isApplicationJson == true) {
        val string = response.body?.string()
        if (string != null) {
            Result(
                Result.Source.API, true,
                Date().time, null,
                mediaType, string
            )
        } else {
            throw RequestMissingBodyException(
                response.request.url.toString(),
                Method.fromString(response.request.method),
                response
            )
        }
    } else {
        throw Result.TypeNotImplementedException()
    }
}
fun Maybe<Response>.string(): Maybe<String> = map { response ->
    response.body?.string()
        ?: throw RequestMissingBodyException(
            response.request.url.toString(),
            Method.fromString(response.request.method),
            response
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