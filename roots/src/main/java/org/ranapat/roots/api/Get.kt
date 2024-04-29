package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.ranapat.roots.api.Base.ensureSuccessful
import org.ranapat.roots.api.Base.setHeaders

object Get {
    fun fromJson(
        url: String,
        okHttpClient: OkHttpClient? = null,
        headers: Map<String, String>? = null
    ): Maybe<Response> {
        return Maybe
            .fromCallable {
                val client = okHttpClient ?: OkHttpClientProvider.client
                val request: Request = Request.Builder().apply {
                    url(url)
                    setHeaders(this, headers)
                }.build()

                return@fromCallable ensureSuccessful(client.newCall(request).execute())
            }
            .subscribeOn(Schedulers.io())
    }

    fun <T : Any> fromJson(
        url: String, valueType: Class<T>,
        okHttpClient: OkHttpClient? = null,
        headers: Map<String, String>? = null,
        normaliseResponse: NormaliseResponse<T>? = null
    ): Maybe<T> = fromJson(url, okHttpClient, headers).like(valueType, normaliseResponse)
}