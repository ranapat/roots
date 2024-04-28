package org.ranapat.roots.api

import okhttp3.Response
import java.io.IOException

data class RequestMissingBodyException(
    val url: String,
    val method: Method,
    val response: Response
) : IOException("$method request to [ $url ] passed with status code ${response.code}, but has empty body")
