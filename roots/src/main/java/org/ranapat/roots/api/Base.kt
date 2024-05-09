package org.ranapat.roots.api

import okhttp3.Request
import okhttp3.Response

abstract class Base {
    protected fun setHeaders(builder: Request.Builder, headers: Map<String, String>?) {
        headers?.forEach { (name, value) ->
            builder.addHeader(name, value)
        }
    }

    @Throws(RequestNotSuccessfulException::class)
    protected fun ensureSuccessful(response: Response): Response {
        if (response.isSuccessful) {
            return response
        } else {
            response.close()
            throw RequestNotSuccessfulException(response.request.url.toString(), response)
        }
    }
}