package org.ranapat.roots.converter

import com.fasterxml.jackson.module.kotlin.readValue

inline fun <reified T> fromJson(
    from: String
): T = ObjectMapperProvider.mapper.readValue<T>(from)