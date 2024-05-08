package org.ranapat.roots.converter

object Converter {
    fun <T> fromJson(
        from: String, valueType: Class<T>
    ): T = ObjectMapperProvider.mapper.readValue(from, valueType)
}