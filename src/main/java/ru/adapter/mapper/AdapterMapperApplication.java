package ru.adapter.mapper;

import ru.adapter.mapper.usecases.MapperUsecase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Main class
 *
 * @author Skyhunter
 * @date 14.10.2022
 */
public class AdapterMapperApplication {

    private final static Logger LOG = Logger.getLogger(AdapterMapperApplication.class.getName());

    public static void main(String[] args) {

        MapperUsecase mapper = new MapperUsecase();
        if (args.length == 2) {
            mapper.execute(
                getStringFromResource(args[0]),
                getStringFromResource(args[1])
            );
        } else {
            mapper.execute(
                getStringFromResource("/schema.json"),
                getStringFromResource("/sourceFile.json")
            );
        }
    }

    private static String getStringFromResource(String resourceName) {
        try( InputStream inputStream = Files.newInputStream(Paths.get(AdapterMapperApplication.class.getResource(resourceName).toURI()))) {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error load string from resource", e);
            return "";
        }
    }

}
