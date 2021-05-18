package io.lw900925.ocean.support.spring.data.jpa.convert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Jackson2MapAttributeConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jackson2MapAttributeConverter.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public Jackson2MapAttributeConverter() {
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        String jsonStr = "";

        try {
            jsonStr = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            LOGGER.error("Convert to database column failure, object details: {}", map.toString());
            LOGGER.debug(e.getMessage(), e);
        }

        return jsonStr;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String jsonStr) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (jsonStr != null && !"".equals(jsonStr) && jsonStr.length() > 0) {
            try {
                map = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
                });
            } catch (IOException e) {
                LOGGER.error("Convert to entity attribute failure, json string: {}", jsonStr);
                LOGGER.debug(e.getMessage(), e);
            }
        }
        return map;
    }
}
