package cn.neptunex.configuration;

import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.driver.ConfigurationDriver;
import cn.neptunex.configuration.driver.YamlConfigurationDriver;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.interfaces.AutoConfiguration;
import cn.neptunex.configuration.proxy.ConfigurationProxy;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                ? processConfigurationGroup(configurationInterface, configuration)
                : processConfiguration(configurationInterface, configuration);
    }

    private static <T extends AutoConfiguration> T processConfigurationGroup(Class<T> configurationInterface, Configuration configuration) throws AutoConfigurationException, IOException {
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
            Path filePath = Paths.get(file.getPath());
            try(BufferedReader reader = Files.newBufferedReader(filePath)){
                ConfigurationDriver driver = new YamlConfigurationDriver(reader);
                InvocationHandler invocationHandler = new ConfigurationProxy<>(configurationInterface, driver);
                T temp = (T) Proxy.newProxyInstance(classLoader, new Class[]{configurationInterface}, invocationHandler);
                groups.add(temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        InvocationHandler invocationHandler = (proxy, method, args) -> {
            if ("groups".equals(method.getName())){
                return groups;
            }else {
                return method.invoke(proxy, args);
            }
        };
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{configurationInterface}, invocationHandler);
    }

    private static <T extends AutoConfiguration> T processConfiguration(Class<T> configurationInterface, Configuration configuration){
        ClassLoader classLoader = configurationInterface.getClassLoader();

        String fileName = configuration.value();

        // FIXME: 这里到时候肯定还要再改, 因为这里到时候可能会有网络IO, 或者绝对路径读取等等, 我这样写只是测试
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
        assert inputStream != null;
        ConfigurationDriver driver = new YamlConfigurationDriver(new BufferedReader(new InputStreamReader(inputStream)));

        InvocationHandler invocationHandler = new ConfigurationProxy<>(configurationInterface, driver);
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{configurationInterface}, invocationHandler);
    }

}
