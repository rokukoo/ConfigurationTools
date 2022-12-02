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
    public void test1() throws AutoConfigurationException, IOException, InterruptedException {
        SettingsConfig settingsConfig = ConfigurationEnhancer.enhance(SettingsConfig.class);
        settingsConfig.setEnable(false);
//        Thread thread = new Thread(() -> {
//            while (true){
//                System.out.println(settingsConfig.isEnable());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        thread.start();
//        Thread.sleep(100000000);
    }

}

@Configuration(root = "settings", autoReload = true)
interface SettingsConfig extends AutoConfiguration {

    boolean isEnable();

    void setEnable(boolean enable);

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