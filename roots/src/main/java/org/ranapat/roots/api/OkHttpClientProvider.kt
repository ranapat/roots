package org.ranapat.roots.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientProvider {
    val client: OkHttpClient by lazy { OkHttpClient() }

    fun client(
        connectionTimeout: Long = 10000,
        writeTimeout: Long = 10000,
        readTimeout: Long = 10000,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
            .build()
    }
}