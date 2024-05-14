package org.ranapat.roots.api

import okhttp3.Response
import java.nio.charset.Charset

internal object ResponseTools {
    private val contentTypeCharsetMatcher = ".*charset=([^ ;]+)".toRegex()

    fun getEncoding(response: Response): Charset? {
        try {
            return Charset.forName(response.headers["Content-Encoding"])
        } catch (e: Exception) {
            //
        }

        val contentType = response.headers["Content-Type"]
        if (contentTypeCharsetMatcher.matches(contentType ?: "")) {
            try {
                return Charset.forName(
                    contentType!!.replace(contentTypeCharsetMatcher, "$1")
                )
            } catch (e: Exception) {
                //
            }
        }

        return null
    }
}