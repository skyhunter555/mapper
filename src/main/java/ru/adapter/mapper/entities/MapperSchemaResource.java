package ru.adapter.mapper.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MapperSchemaTarget
 * @author Skyhunter
 * @date 14.10.2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapperSchemaResource implements Serializable {

    private String type;
    private String title;
    private List<String> required;
    private MapperSchemaResourceProperties properties;

}
