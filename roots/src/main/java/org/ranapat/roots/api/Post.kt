package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.ranapat.roots.api.Base.ensureSuccessful
import org.ranapat.roots.api.Base.setHeaders

object Post {
    fun json(
        url: String,
        content: String,
        mediaType: MediaType? = null,
        okHttpClient: OkHttpClient? = null,
        headers: Map<String, String>? = null,
    ): Maybe<Response> {
        return Maybe
            .fromCallable {
                val client = okHttpClient ?: OkHttpClientProvider.client
                val request: Request = Request.Builder().apply {
                    url(url)
                    setHeaders(this, headers)
                    post(content.toRequestBody(mediaType))
                }.build()

                return@fromCallable ensureSuccessful(client.newCall(request).execute())
            }
            .subscribeOn(Schedulers.io())
    }

    fun <T : Any> json(
        url: String, content: String, valueType: Class<T>,
        mediaType: MediaType? = null,
        okHttpClient: OkHttpClient? = null,
        headers: Map<String, String>? = null,
        normaliseResponse: NormaliseResponse<T>? = null
    ): Maybe<T> = json(url, content, mediaType, okHttpClient, headers).like(valueType, normaliseResponse)
}