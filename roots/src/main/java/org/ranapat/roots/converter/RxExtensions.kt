package org.ranapat.roots.converter

import io.reactivex.rxjava3.core.Maybe
import org.ranapat.roots.Result

object StringConverter {
    fun <T : Any> Maybe<String>.instance(valueType: Class<T>): Maybe<T> = map { string ->
        Converter.fromJson(string, valueType)
    }
}

object ResultConverter {
    fun <T : Any> Maybe<Result>.instance(valueType: Class<T>): Maybe<T> = map { result ->
        if (result.success) {
            Converter.fromJson(result.contentValue(), valueType)
        } else {
            throw ConvertFailedException()
        }
    }
}