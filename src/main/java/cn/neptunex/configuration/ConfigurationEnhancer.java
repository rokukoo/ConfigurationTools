package cn.neptunex.configuration;

import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.driver.ConfigurationDriver;
import cn.neptunex.configuration.driver.YamlConfigurationDriver;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.features.AutoConfiguration;
import cn.neptunex.configuration.features.ConfigurationReloadCallback;
import cn.neptunex.configuration.proxy.ConfigurationProxy;
import lombok.val;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ConfigurationEnhancer {

    public static File PARENT;

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

    private static <T extends AutoConfiguration> T enhanceConfigurationGroup(Class<T> configurationInterface, Configuration configuration) throws AutoConfigurationException {
        boolean isGroup = configuration.group();
        String value = configuration.value();
        String folder = configuration.folder();
        if (!isGroup) throw new AutoConfigurationException("The configuration annotation present on AutoConfigurationGroup group must be set to true.");
        if (!"".equals(value) && folder.equals("")) throw new AutoConfigurationException("AutoConfigurationGroup should use folder instead of value.");

        ClassLoader classLoader = configurationInterface.getClassLoader();
        List<T> groups = new LinkedList<>();

        File dataFolder = new File(PARENT, folder);
        Arrays.stream(Objects.requireNonNull(dataFolder.listFiles())).forEach(file -> {
            val proxy = enhanceConfiguration(file, configurationInterface, configuration);
            groups.add(proxy);
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
        File file = new File(PARENT, fileName);
        return enhanceConfiguration(file, configurationInterface, configuration);
    }

    private static <T extends AutoConfiguration> T enhanceConfiguration(File file, Class<T> configurationInterface, Configuration configuration){
        val isAutoReload = configuration.autoReload();
        val callbackClz = configuration.reloadCallback();
        val driver = new YamlConfigurationDriver(file, isAutoReload, callbackClz);
        val proxyInstance = newProxyInstance(configurationInterface, driver);
        driver.setInstance(proxyInstance);
        return proxyInstance;
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
