package org.ranapat.roots.api

import okhttp3.Response
import java.nio.charset.Charset

private val contentTypeCharsetMatcher = ".*charset=([^ ;]+)".toRegex()

val Response.encoding: Charset?
    get() {
        try {
            return Charset.forName(headers["Content-Encoding"])
        } catch (e: Exception) {
            //
        }

        try {
            val contentType = headers["Content-Type"]
            if (contentTypeCharsetMatcher.matches(contentType ?: "")) {
                return Charset.forName(
                    contentType!!.replace(contentTypeCharsetMatcher, "$1")
                )

            }
        } catch (e: Exception) {
            //
        }

        return null
    }