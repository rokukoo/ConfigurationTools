package cn.neptunex.configuration.features;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

@FunctionalInterface
public interface ConfigurationReloadCallback {

    void acceptReload(File file, FileConfiguration fileConfiguration);

}
