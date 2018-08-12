package org.matrixstudio.ocean.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <p>fasterxml jackson mapper utils.</p>
 *
 * @author liuwei
 */
public class JacksonMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonMapper.class);

    /**
     * {@link ObjectMapper}, initialize by constructor method
     */
    private ObjectMapper objectMapper;

    /**
     * <p>Initialize, must provide a {@link ObjectMapper} object.</p>
     *
     * @param objectMapper a {@link ObjectMapper} object
     */
    public JacksonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public <T> T read(String jsonStr, Class<T> clazz) {
        T object = null;
        try {
            object = objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return object;
    }

    public <T> T read(String jsonStr, TypeReference<T> typeReference) {
        T object = null;
        try {
            object = objectMapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return object;
    }

    public String write(Object object) {
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return jsonStr;
    }
}
