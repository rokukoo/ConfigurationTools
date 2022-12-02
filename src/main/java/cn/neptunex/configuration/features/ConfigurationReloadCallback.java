package cn.neptunex.configuration.features;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

@FunctionalInterface
public interface ConfigurationReloadCallback<T extends AutoConfiguration> {

    void acceptReload(File file, T config);

}
