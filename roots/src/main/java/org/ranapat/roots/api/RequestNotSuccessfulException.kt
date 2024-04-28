package org.ranapat.roots.api

import okhttp3.Response
import java.io.IOException

data class RequestNotSuccessfulException(
    val url: String,
    val method: Method,
    val response: Response
) : IOException("$method request to [ $url ] failed with status code ${response.code}")