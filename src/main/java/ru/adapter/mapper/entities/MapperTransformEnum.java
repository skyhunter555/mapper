package ru.adapter.mapper.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Skyhunter
 * @date 14.10.2022
*/
public enum MapperTransformEnum {

    STRING_TO_INTEGER( "string_to_integer"),
    INTEGER_TO_STRING( "integer_to_string");

    @JsonProperty("transform")
    private final String code;
    MapperTransformEnum(String code) {
        this.code = code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MapperTransformEnum forValues(@JsonProperty("transform") String code) {
        for (MapperTransformEnum each : values()) {
            if (each.code.equals(code)) {
                return each;
            }
        }
        return null;
    }

}
