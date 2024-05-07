package org.ranapat.roots.converter

import com.fasterxml.jackson.module.kotlin.readValue

object Converter {
    fun <T> from(
        from: String, valueType: Class<T>
    ): T = ObjectMapperProvider.mapper.readValue(from, valueType)

    inline fun <reified T> from(
        from: String
    ): T = ObjectMapperProvider.mapper.readValue<T>(from)
}