package com.kelvin.ms_app.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class JsonUtil {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private final static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static boolean isJsonValid(String jsonInString) {
        try {
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String toJson(Object obj) {
        String json = null;
        try {
            json = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Parse json error", e);
        }
        return json;
    }

    public static String toJsonPrettyPrint(Object obj) {
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Parse json error", e);
        }
        return json;
    }

    public static JsonNode toJsonNode(Object obj) {
        JsonNode json = mapper.nullNode();
        try {
            if (obj instanceof String) {
                json = mapper.readValue((String) obj, JsonNode.class);
            } else {
                json = mapper.valueToTree(obj);
            }
        } catch (JsonProcessingException e) {
            logger.error("Parse json error", e);
        }
        return json;
    }

    public static <T> T fromJson(String jsonInString, Class<T> classOfT) {
        T obj = null;
        try {
            obj = mapper.readValue(jsonInString, classOfT);
        } catch (Exception e) {
            logger.error("Parse json error", e);
        }
        return obj;
    }

    public static HashMap<String,Object> fromJsonToHashMap(String jsonInString) {
        HashMap<String,Object> obj = new HashMap<>();
        try {
            obj = mapper.readValue(jsonInString, new TypeReference<HashMap<String, Object>>() {});
        } catch (Exception e) {
            logger.error("fromJsonToHashMap Error",e);
        }
        return obj;
    }

    public static <T> List<T> fromJsonToList(String jsonInString, Class<T> classOfT) {
        List<T> obj = null;
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            obj = mapper.readValue(jsonInString, mapper.getTypeFactory().constructCollectionType(List.class, classOfT));
        } catch (Exception e) {
            logger.error("Parse Object List Failed", e);
        }
        return obj;
    }

    public static <T> T convertValue(Object value, Class<T> clazz) {
        T obj = null;
        try {
            obj = mapper.convertValue(value, clazz);
        } catch (Exception e) {
            logger.error("Prase object list failed", e);
        }
        return obj;
    }

    public static ParseContext getJsonPathParser() {
        return JsonPath.using(Configuration.builder()
                .jsonProvider(new JacksonJsonProvider(mapper))
                .mappingProvider(new JacksonMappingProvider(mapper))
                .build());
    }

    public static <T> T jsonParse(Object object, String jsonPath, Class<T> classOfT) {
        T value = null;
        try {
            value = getJsonPathParser().parse(object).read(jsonPath, classOfT);
        }catch(PathNotFoundException e){
            logger.error(e.getMessage());
        }catch (Exception e) {
            logger.error("Parse json error", e);
        }
        return value;
    }

}
