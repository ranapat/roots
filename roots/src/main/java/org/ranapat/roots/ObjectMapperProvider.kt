package org.ranapat.roots

import com.fasterxml.jackson.databind.ObjectMapper

object ObjectMapperProvider {
    val mapper: ObjectMapper by lazy { ObjectMapper() }

    fun mapper(): ObjectMapper {
        return ObjectMapper()
    }
}