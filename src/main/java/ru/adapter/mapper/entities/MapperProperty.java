package ru.adapter.mapper.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * MapperProperty
 * @author Skyhunter
 * @date 14.10.2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapperProperty implements Serializable {
    private String type;
    private String title;
    @JsonProperty("default")
    private Object defaultValue;
    private List<Object> examples;
}
