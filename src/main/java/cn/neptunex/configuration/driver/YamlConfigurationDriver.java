package cn.neptunex.configuration.driver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

public class YamlConfigurationDriver implements ConfigurationDriver {

    private final FileConfiguration fileConfiguration;

    public YamlConfigurationDriver(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public YamlConfigurationDriver(Reader reader) {
        this(YamlConfiguration.loadConfiguration(reader));
    }

    public YamlConfigurationDriver(File file) {
        this(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public <T> T get(String path, Class<T> returnType) {
        if (returnType.equals(String.class)){
            fileConfiguration.getString(path);
        }
        return (T) fileConfiguration.get(path);
    }

    @Override
    public <T> void set(String path, T value) {
        fileConfiguration.set(path, value);
    }

    @Override
    public void save() throws IOException {
        fileConfiguration.save(new File(fileConfiguration.getCurrentPath()));
    }

}
