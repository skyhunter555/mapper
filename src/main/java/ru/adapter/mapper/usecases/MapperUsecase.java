package ru.adapter.mapper.usecases;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.adapter.mapper.AdapterMapperApplication;
import ru.adapter.mapper.entities.MapperProperty;
import ru.adapter.mapper.entities.MapperSchema;
import ru.adapter.mapper.entities.MapperSchemaField;
import ru.adapter.mapper.entities.MapperSchemaResource;
import ru.adapter.mapper.entities.MapperTypeEnum;
import ru.adapter.mapper.utils.MapperUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MapperUsecase
 * @author Skyhunter
 * @date 14.10.2022
 */
public class MapperUsecase {

    private final static Logger LOG = Logger.getLogger(AdapterMapperApplication.class.getName());

    public String execute(String schema, String source) {

        //Получение схемы маппинга
        ObjectMapper mapper = new ObjectMapper();
        MapperSchema mapperSchema = null;
        try {
            mapperSchema = mapper.readValue(schema, MapperSchema.class);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error load mapperSchema from schema", e);
            return null;
        }

        //Получение схемы маппинга
        JsonParser jsonParser;
        try {
            jsonParser = new JsonFactory().createParser(source);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error load jsonParser from source", e);
            throw new RuntimeException(e);
        }

        Map<String, Object> sourceValuesMap;
        try {
            sourceValuesMap = MapperUtil.getTargetValuesMap(jsonParser);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Error load targetValuesMap from jsonParser", e);
            throw new RuntimeException(e);
        }

        String result = createJsonString(
                mapperSchema.getTargetSchema(),
                sourceValuesMap,
                mapperSchema.getMapping()
        );

        LOG.log(Level.INFO, result);

        return result;
    }

    /**
     * Получение строки в формате JSON из набора исходных значений и схемы маппинга
     * @param targetSchema    - target схема
     * @param sourceValuesMap - набор значений исходных данных
     * @param mapping         - схема маппинга полей
     * @return
     */
    private String createJsonString(MapperSchemaResource targetSchema, Map<String, Object> sourceValuesMap, List<MapperSchemaField> mapping) {
        ObjectMapper targetMapper = new ObjectMapper();
        ObjectNode rootNode = targetMapper.createObjectNode();

        for (Map.Entry<String, Object> entry : targetSchema.getProperties().getAdditionalProperties().entrySet()) {
            MapperProperty targetProperty = MapperUtil.getMapperProperty(entry.getValue());

            //Признак обязательности поля
            Optional<String> fieldRequired = targetSchema.getRequired().stream()
                    .filter(schemaField -> entry.getKey().equals(schemaField)).findAny();

            //Находим значение target поля
            Object targetValue = getTargetFieldValue(
                    mapping,
                    entry.getKey(),
                    targetProperty,
                    sourceValuesMap,
                    fieldRequired.isPresent()
            );
            //Добавление target поля
            if (targetValue != null) {
                putValueToRootNode(rootNode, entry.getKey(), targetValue, targetProperty.getType());
            }
        }
        try {
            return targetMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error rootNode writeValueAsString", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение значения target поля по имени поля из набора значений исходных данных
     *
     * @param mapping         - схема маппинга полей
     * @param targetFieldName - имя target поля
     * @param targetProperty  - свойства target поля
     * @param sourceValuesMap - набор значений исходных данных
     * @param fieldRequired   - признак обязательности поля
     * @return
     */
    private Object getTargetFieldValue(
            List<MapperSchemaField> mapping,
            String targetFieldName,
            MapperProperty targetProperty,
            Map<String, Object> sourceValuesMap,
            Boolean fieldRequired
    ) {

        //Находим в мапинге схему по значению target поля
        MapperSchemaField fieldSchema = mapping.stream()
                .filter(schemaField -> targetFieldName.equals(schemaField.getTarget()))
                .findAny()
                .orElse(null);

        //Если схема есть пытаемся найти значение в исходных данных
        if (fieldSchema != null) {
            Object targetValue = sourceValuesMap.get(fieldSchema.getSource());
            if (targetValue != null) {
                return targetValue;
            }
        }

        //Если поле обязательное, необходимо проставить значение по умолчанию
        if (fieldRequired) {
            //Исходим из того, что у обязательно поля не может быть значение null
            return targetProperty.getDefaultValue();
        }
        return null;
    }

    /**
     * Добавление target поля. Пока только для трех типов значений
     *
     * @param rootNode    - корневой узел
     * @param targetKey   - имя target поля
     * @param targetValue - значение target поля
     * @param targetType  - тип target поля
     */
    private void putValueToRootNode(ObjectNode rootNode, String targetKey, Object targetValue, String targetType) {
        if (MapperTypeEnum.STRING.getCode().equals(targetType)) {
            rootNode.put(targetKey, targetValue.toString());
        } else if (MapperTypeEnum.INTEGER.getCode().equals(targetType)) {
            rootNode.put(targetKey, Integer.valueOf(targetValue.toString()));
        } else if (MapperTypeEnum.BOOLEAN.getCode().equals(targetType)) {
            rootNode.put(targetKey, Boolean.valueOf(targetValue.toString()));
        }
    }


}
