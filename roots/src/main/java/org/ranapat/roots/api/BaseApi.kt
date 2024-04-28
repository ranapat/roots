package org.ranapat.roots.api

import okhttp3.Request

interface BaseApi {
    fun applyHeaders(builder: Request.Builder, headers: Map<String, String>?) {
        headers?.forEach { (name, value) ->
            builder.addHeader(name, value)
        }
    }
}