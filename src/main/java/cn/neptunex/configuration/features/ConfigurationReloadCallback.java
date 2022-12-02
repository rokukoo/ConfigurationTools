package cn.neptunex.configuration.features;

import org.bukkit.configuration.file.FileConfiguration;

@FunctionalInterface
public interface ConfigurationReloadCallback {

    void acceptReload(FileConfiguration fileConfiguration);

}
