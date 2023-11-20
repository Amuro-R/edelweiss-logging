package org.edelweiss.logging.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

import java.util.TimeZone;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
public class JsonUtil {

    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public static String toJsonString(Object data) {
        return OBJECT_MAPPER.writeValueAsString(data);
    }

    @SneakyThrows
    public static <T> T fromJsonString(String json, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    @SneakyThrows
    public static <T> T fromJsonString(JsonNode node, Class<T> clazz) {
        JsonParser jsonParser = node.traverse();
        return OBJECT_MAPPER.readValue(jsonParser, clazz);
    }

    @SneakyThrows
    public static <T> T fromJsonString(JsonNode node, TypeReference<T> typeReference) {
        JsonParser jsonParser = node.traverse();
        return OBJECT_MAPPER.readValue(jsonParser, typeReference);
    }

    @SneakyThrows
    public static <T> T fromJsonString(String json, TypeReference<T> typeReference) {
        return OBJECT_MAPPER.readValue(json, typeReference);
    }


    @SneakyThrows
    public static JsonNode toJsonNode(String json) {
        return OBJECT_MAPPER.readTree(json);
    }

    public static JsonNode toJsonNode(JsonNode node, String property) {
        return node.get(property);
    }
}
