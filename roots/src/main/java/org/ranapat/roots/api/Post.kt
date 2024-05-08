package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

object Post : Base() {
    fun from(
        url: String,
        content: String,
        mediaType: MediaType? = null,
        headers: Map<String, String>? = null,
        okHttpClient: OkHttpClient? = null,
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
}