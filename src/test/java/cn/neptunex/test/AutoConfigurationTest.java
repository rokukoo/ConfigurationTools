package cn.neptunex.test;

import cn.neptunex.configuration.annotations.Binding;
import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.interfaces.AutoConfiguration;
import cn.neptunex.configuration.ConfigurationProcessor;
import org.junit.jupiter.api.Test;

import java.io.*;

public class AutoConfigurationTest {

    @Test
    public void test1() throws FileNotFoundException, AutoConfigurationException {
        MyConfig myConfig = ConfigurationProcessor.process(MyConfig.class);
        boolean isEnabled = myConfig.isEnabled();
        System.out.println(isEnabled);
    }

}

@Configuration(value = "config.yml")
interface MyConfig extends AutoConfiguration {

    @Binding("settings.enable")
    boolean isEnabled();

}
