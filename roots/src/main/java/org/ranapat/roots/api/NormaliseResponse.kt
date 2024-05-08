package org.ranapat.roots.api

interface NormaliseResponse<T> {
    fun invoke(from: String): T
}