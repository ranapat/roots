package org.ranapat.roots.converter

import io.reactivex.rxjava3.core.Maybe

fun <T: Any> Maybe<String>.instance(valueType: Class<T>): Maybe<T> = map { string ->
    Converter.fromJson(string, valueType)
}