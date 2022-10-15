package ru.adapter.mapper.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * MapperSchemaField model
 *
 * @author Skyhunter
 * @date 14.10.2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapperSchemaField implements Serializable {

    private String target;
    private String source;
    private MapperTransformEnum transform;

}
