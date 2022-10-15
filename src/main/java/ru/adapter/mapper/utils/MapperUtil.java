package ru.adapter.mapper.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.val;
import ru.adapter.mapper.entities.MapperProperty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Преобразует тип данных из json в map со значениями полей.
 *
 * @author Skyhunter
 * @date 14.10.2022
 */
public class MapperUtil {

    public static Map<String, Object> getTargetValuesMap(JsonParser jsonParser) throws IOException {

        Map<String, Object> targetMap = new HashMap<>();
        JsonToken token = jsonParser.nextToken();
        while (token != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                String fieldSource = jsonParser.getCurrentName();
                jsonParser.nextToken();
                targetMap.put(fieldSource, jsonParser.getText());
            }
            token = jsonParser.nextToken();
        }
        return targetMap;
    }

    public static MapperProperty getMapperProperty(Object propertyMap) {
        MapperProperty property = new MapperProperty();
        property.setType(getStringPropertyByKey(propertyMap, "type"));
        property.setDefaultValue(getStringPropertyByKey(propertyMap, "default"));
        property.setTitle(getStringPropertyByKey(propertyMap, "title"));
        return property;
    }

    public static String getStringPropertyByKey(Object propertyMap, String key) {
        if (propertyMap instanceof Map) {
            val propertyValue = ((Map<?, ?>) propertyMap).get(key);
            if (propertyValue != null) {
                return propertyValue.toString();
            }
        }
        return null;
    }
}
