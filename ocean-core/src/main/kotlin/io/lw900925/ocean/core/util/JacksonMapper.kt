package io.lw900925.ocean.core.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class JacksonMapper(var objectMapper: ObjectMapper) {

    val LOGGER: Logger = LoggerFactory.getLogger(JacksonMapper::class.java)

    fun <T> read(jsonStr: String, clazz: Class<T>): T? {
        return objectMapper.readValue(jsonStr, clazz)
    }

    fun <T> read(jsonStr: String, typeReference: TypeReference<T>): T? {
        return objectMapper.readValue(jsonStr, typeReference)
    }

    fun write(`object`: Any): String {
        return objectMapper.writeValueAsString(`object`)
    }
}