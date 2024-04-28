package org.ranapat.roots.api

import okhttp3.Response

interface NormaliseResponse<T> {
    fun invoke(response: Response): T
}