package org.ranapat.roots.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

object ObjectMapperProvider {
    val mapper: ObjectMapper by lazy { ObjectMapper().registerModules(KotlinModule.Builder().build()) }

    fun mapper(): ObjectMapper {
        return ObjectMapper().registerModules(KotlinModule.Builder().build())
    }
}