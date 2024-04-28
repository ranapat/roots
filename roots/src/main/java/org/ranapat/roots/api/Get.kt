package org.ranapat.roots.api

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.ranapat.roots.ObjectMapperProvider
import java.io.IOException

fun <T : Any> getFromJson(
    url: String, valueType: Class<T>,
    okHttpClient: OkHttpClient? = null,
    headers: Map<String, String>? = null,
    normaliseResponse: ((response: Response) -> T)? = null
): Maybe<T> {
    return Maybe
        .fromCallable<T> {
            val client = okHttpClient ?: OkHttpClientProvider.client
            val builder: Request.Builder = Request.Builder()
                .url(url)
            headers?.let { headers ->
                headers.forEach { (name, value) ->
                    builder.addHeader(name, value)
                }
            }
            val request: Request = builder.build()
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return@fromCallable normaliseResponse?.invoke(response)
                        ?: ObjectMapperProvider.mapper.readValue(response.body?.string(), valueType)
                } else {
                    throw RequestNotSuccessfulException(url, Method.GET, response)
                }
            } catch (e: IOException) {
                throw e
            }
        }
        .subscribeOn(Schedulers.io())
}