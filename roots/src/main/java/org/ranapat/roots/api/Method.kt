package org.ranapat.roots.api

import java.util.Locale

enum class Method(val value: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    UNDEFINED("UNDEFINED");

    companion object {
        fun fromString(string: String): Method {
            return entries.firstOrNull { it.name == string.uppercase(Locale.getDefault()) } ?: UNDEFINED
        }
    }
}