package com.enotakin.library.api.configuration;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class ConfigurationProvider {

    public static final ConfigurationProvider YAML = new YamlConfiguration();
    public static final ConfigurationProvider JSON = new JsonConfiguration();

    public Configuration load(Plugin plugin, String fileName, String backupFileName) {
        InputStream inputStream = plugin.getResource(backupFileName);
        if (inputStream == null) {
            throw new IllegalStateException();
        }

        Map<String, Object> map = load(plugin.getDataFolder(), inputStream, fileName);
        return new Configuration(map, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Map<String, Object> load(File dataFolder, InputStream inputStream, String fileName) {
        dataFolder.mkdirs();

        File file = new File(dataFolder, fileName);

        try {
            if (file.createNewFile()) {
                try (OutputStream outputStream = new FileOutputStream(file, false)) {
                    inputStream.transferTo(outputStream);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load config:", exception);
        }

        return load(file);
    }

    public abstract Map<String, Object> load(File file);

    public abstract void save(Configuration config, File file) throws IOException;

    public abstract void save(Configuration config, Writer writer);

}
