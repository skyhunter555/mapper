package ru.adapter.mapper.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MapperSchema model
 *
 * @author Skyhunter
 * @date 14.10.2022
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapperSchema implements Serializable {

    private List<MapperSchemaField> mapping;
    private MapperSchemaResource sourceSchema;
    private MapperSchemaResource targetSchema;
}
