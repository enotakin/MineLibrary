package com.enotakin.library.api.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonConfiguration extends ConfigurationProvider {

    private final Gson json = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(Configuration.class, (JsonSerializer<Configuration>) (src, typeOfSrc, context) -> context.serialize(src.self))
            .create();

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> load(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            Map<String, Object> map = json.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), LinkedHashMap.class);
            if (map == null) {
                map = new LinkedHashMap<>();
            }
            return map;
        } catch (IOException exception) {
            throw new IllegalStateException("", exception);
        }
    }

    @Override
    public void save(Configuration config, File file) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            save(config, writer);
        }
    }

    @Override
    public void save(Configuration config, Writer writer) {
        json.toJson(config.self, writer);
    }

}
