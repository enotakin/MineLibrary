package com.enotakin.library.api.configuration;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class YamlConfiguration extends ConfigurationProvider {

    private final ThreadLocal<Yaml> yaml = ThreadLocal.withInitial(() -> {
        Representer representer = new Representer() {
            {
                representers.put(Configuration.class, data -> represent(((Configuration) data).self));
            }
        };

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        return new Yaml(new Constructor(), representer, options);
    });

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> load(File file) {
        try (FileInputStream is = new FileInputStream(file)) {
            Map<String, Object> map = yaml.get().loadAs(is, LinkedHashMap.class);
            if (map == null) {
                map = new LinkedHashMap<>();
            }
            return map;
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
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
        yaml.get().dump(config.self, writer);
    }

}
