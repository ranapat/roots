package org.ranapat.roots.converter

import org.ranapat.roots.ObjectMapperProvider

fun <T> toTyped(
    content: String, valueType: Class<T>
): T =  ObjectMapperProvider.mapper.readValue(content, valueType)
