package org.ranapat.roots

fun <T> toTyped(
    content: String, valueType: Class<T>
): T =  ObjectMapperProvider.mapper.readValue(content, valueType)
