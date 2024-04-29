package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

object Get : BaseApi() {
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
                    setHeaders(this, headers)
                }.build()

                return@fromCallable toTyped(
                    client.newCall(request).execute(),
                    valueType, normaliseResponse
                )
            }
            .subscribeOn(Schedulers.io())
    }
}