package cn.neptunex.test;

import cn.neptunex.configuration.annotations.Binding;
import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.ConfigurationEnhancer;
import cn.neptunex.configuration.features.AutoConfiguration;
import cn.neptunex.configuration.features.AutoConfigurationGroup;
import cn.neptunex.configuration.features.ConfigurationReloadCallback;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.Test;

import java.io.*;

public class AutoConfigurationTest {

    @Test
    public void test1() throws AutoConfigurationException, IOException, InterruptedException {

        ConfigurationEnhancer.PARENT = new File("D:\\rokuko\\projects\\configuration\\src\\test\\resources");

        ItemsConfig itemsConfig = ConfigurationEnhancer.enhance(ItemsConfig.class);
        itemsConfig.groups().forEach(it -> {

        });
        Thread.sleep(100000000);
    }

}

class TestCallBack implements ConfigurationReloadCallback<ItemsConfig>{

    @Override
    public void acceptReload(File file, ItemsConfig config) {
        System.out.println("来自文件: " + file.getName() + "的改动, name = "+ config.getName());
    }

}

@Configuration(group = true, folder = "items", autoReload = true, reloadCallback = TestCallBack.class)
interface ItemsConfig extends AutoConfigurationGroup<ItemsConfig> {

    String getName();
    String getPrefix();

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