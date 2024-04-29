package org.ranapat.roots.api

import okhttp3.Request
import okhttp3.Response
import org.ranapat.roots.ObjectMapperProvider

object BaseApi {
    fun setHeaders(builder: Request.Builder, headers: Map<String, String>?) {
        headers?.forEach { (name, value) ->
            builder.addHeader(name, value)
        }
    }

    @Throws(
        RequestNotSuccessfulException::class
    )
    fun ensureSuccessful(response: Response): Response {
        if (response.isSuccessful) {
            return response
        } else {
            throw RequestNotSuccessfulException(response.request.url.toString(), response)
        }
    }

    @Throws(
        RequestMissingBodyException::class
    )
    fun <T> toTyped(
        response: Response, valueType: Class<T>,
        normaliseResponse: NormaliseResponse<T>?
    ): T {
        val body = response.body
        if (body != null) {
            return if (normaliseResponse != null) {
                normaliseResponse.invoke(response)
            } else {
                ObjectMapperProvider.mapper.readValue(body.string(), valueType)
            }
        } else {
            throw RequestMissingBodyException(response.request.url.toString(), Method.GET, response)
        }
    }
}