package org.ranapat.roots.api

import okhttp3.Response
import java.io.IOException

data class RequestNotSuccessfulException(
    val url: String,
    val response: Response
) : IOException("${response.request.method} request to [ $url ] failed with status code ${response.code}")