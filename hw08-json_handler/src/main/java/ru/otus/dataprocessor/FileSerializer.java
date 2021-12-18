package ru.otus.dataprocessor;

import javax.json.Json;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл
        final var objectBuilder = Json.createObjectBuilder();
        data.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(entry -> objectBuilder.add(entry.getKey(), entry.getValue()));

        var jsonObject = objectBuilder.build();

        try (var fileWriter = Files.newBufferedWriter(Path.of(fileName));
             var stringWriter = new StringWriter()) {
            Json.createWriter(stringWriter).writeObject(jsonObject);
            fileWriter.write(stringWriter.toString());
        } catch (IOException e) {
            throw new FileProcessException(e);
        }

    }
}
