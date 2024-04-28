package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.ranapat.roots.ObjectMapperProvider

object Get : BaseApi {
    fun <T : Any> fromJson(
        url: String, valueType: Class<T>,
        okHttpClient: OkHttpClient? = null,
        headers: Map<String, String>? = null,
        normaliseResponse: NormaliseResponse<T>? = null
    ): Maybe<T> {
        return Maybe
            .fromCallable<T> {
                val client = okHttpClient ?: OkHttpClientProvider.client
                val request: Request = Request.Builder().apply {
                    url(url)
                    applyHeaders(this, headers)
                }.build()

                return@fromCallable toTyped(
                    client.newCall(request).execute(),
                    valueType, normaliseResponse
                )
            }
            .subscribeOn(Schedulers.io())
    }

    @Throws(
        RequestNotSuccessfulException::class,
        RequestMissingBodyException::class
    )
    private fun <T> toTyped(
        response: Response, valueType: Class<T>,
        normaliseResponse: NormaliseResponse<T>?
    ): T {
        if (response.isSuccessful) {
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
        } else {
            throw RequestNotSuccessfulException(response.request.url.toString(), Method.GET, response)
        }
    }
}