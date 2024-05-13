package org.ranapat.roots.api

import java.util.Locale

enum class Method {
    GET,
    POST,
    PUT,
    DELETE,
    UNDEFINED;

    companion object {
        fun fromString(string: String): Method {
            return entries.firstOrNull { it.name == string.uppercase(Locale.getDefault()) } ?: UNDEFINED
        }
    }
}