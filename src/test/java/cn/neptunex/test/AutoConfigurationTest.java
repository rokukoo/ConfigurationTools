package cn.neptunex.test;

import cn.neptunex.configuration.annotations.Binding;
import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.ConfigurationEnhancer;
import cn.neptunex.configuration.interfaces.AutoConfiguration;
import cn.neptunex.configuration.interfaces.AutoConfigurationGroup;
import org.junit.jupiter.api.Test;

import java.io.*;

public class AutoConfigurationTest {

    @Test
    public void test1() throws AutoConfigurationException, IOException {
        SettingsConfig settingsConfig = ConfigurationEnhancer.enhance(SettingsConfig.class);
        settingsConfig.isEnabled();
    }

}

@Configuration
interface SettingsConfig extends AutoConfiguration {
    @Binding("settings.enable")
    boolean isEnabled();

    @Binding("settings.debug")
    boolean isDebug();

}

//@Configuration(group = true, folder = "items")
//interface MyConfigGroup extends AutoConfigurationGroup<MyConfigGroup> {
//
//    @Binding("name")
//    String getName();
//
//    @Binding("prefix")
//    String getPrefix();
//
//}