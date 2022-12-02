package cn.neptunex.configuration;

import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.driver.ConfigurationDriver;
import cn.neptunex.configuration.driver.YamlConfigurationDriver;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.features.AutoConfiguration;
import cn.neptunex.configuration.proxy.ConfigurationProxy;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ConfigurationEnhancer {

    private static boolean checkConfigurationAnnotation(Class<?> configurationInterface){
        return configurationInterface.isAnnotationPresent(Configuration.class);
    }

    // Instantiate the configuration class interface by proxy
    public static <T extends AutoConfiguration> T enhance(Class<T> configurationInterface) throws AutoConfigurationException, IOException {
        // Check whether the AutoConfiguration interface has configuration annotation
        boolean hasAnnotation = checkConfigurationAnnotation(configurationInterface);
        if (!hasAnnotation) throw new AutoConfigurationException("AutoConfiguration interface must be decorated with Configuration annotation.");
        Configuration configuration = configurationInterface.getAnnotation(Configuration.class);
        return configuration.group()
                ? enhanceConfigurationGroup(configurationInterface, configuration)
                : enhanceConfiguration(configurationInterface, configuration);
    }

    private static <T extends AutoConfiguration> T enhanceConfigurationGroup(Class<T> configurationInterface, Configuration configuration) throws AutoConfigurationException, IOException {
        boolean isGroup = configuration.group();
        String value = configuration.value();
        String folder = configuration.folder();
        if (!isGroup) throw new AutoConfigurationException("The configuration annotation present on AutoConfigurationGroup group must be set to true.");
        if (!"".equals(value) && folder.equals("")) throw new AutoConfigurationException("AutoConfigurationGroup should use folder instead of value.");

        ClassLoader classLoader = configurationInterface.getClassLoader();
        List<T> groups = new LinkedList<>();

//        File parent = BukkitPlatformModule.getINSTANCE().getDataFolder();
        // FIXME: 这个地方到时候要视情况而定, 我这里只是做测试
        File parent = new File("D:\\rokuko\\projects\\configuration\\src\\test\\resources");

        File dataFolder = new File(parent, folder);
        Arrays.stream(Objects.requireNonNull(dataFolder.listFiles())).forEach(file -> {
            ConfigurationDriver driver = new YamlConfigurationDriver(file);
            T temp = newProxyInstance(configurationInterface, driver);
            groups.add(temp);
        });

        InvocationHandler invocationHandler = (proxy, method, args) -> {
            if ("groups".equals(method.getName())){
                return groups;
            }else {
                return method.invoke(proxy, args);
            }
        };
        return newProxyInstance(classLoader, new Class[]{configurationInterface}, invocationHandler);
    }

    private static <T extends AutoConfiguration> T enhanceConfiguration(Class<T> configurationInterface, Configuration configuration){
        String fileName = configuration.value();
        boolean isAutoReload = configuration.autoReload();

        // FIXME: 这里到时候肯定还要再改, 因为这里到时候可能会有网络IO, 或者绝对路径读取等等, 我这样写只是测试
        File parent = new File("D:\\rokuko\\projects\\configuration\\src\\test\\resources");

        File file = new File(parent, fileName);
        ConfigurationDriver driver = new YamlConfigurationDriver(file, isAutoReload);
        return newProxyInstance(configurationInterface, driver);
    }

    private static <T extends AutoConfiguration> T newProxyInstance(Class<T> configurationInterface, ConfigurationDriver configurationDriver){
        ClassLoader classLoader = configurationInterface.getClassLoader();
        InvocationHandler invocationHandler = new ConfigurationProxy<>(configurationInterface, configurationDriver);
        return newProxyInstance(classLoader, new Class[]{configurationInterface}, invocationHandler);
    }

    @SuppressWarnings("unchecked")
    private static <T extends AutoConfiguration> T newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h){
        return (T) Proxy.newProxyInstance(loader, interfaces, h);
    }

}
