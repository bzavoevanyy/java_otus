package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try (var jsonReader = Json.createReader(ResourcesFileLoader.class.getClassLoader()
                .getResourceAsStream(fileName))) {
            JsonStructure jsonFromTheFile = jsonReader.read();
            return jsonFromTheFile.asJsonArray().stream().map(JsonValue::asJsonObject)
                    .map(o -> new Measurement(o.getString("name"), o.getInt("value"))).toList();
        }
    }
}
