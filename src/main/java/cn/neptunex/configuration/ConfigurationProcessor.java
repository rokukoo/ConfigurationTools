package cn.neptunex.configuration;

import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.exceptions.AutoConfigurationException;
import cn.neptunex.configuration.interfaces.AutoConfiguration;
import cn.neptunex.configuration.interfaces.AutoConfigurationGroup;
import cn.neptunex.configuration.proxy.ConfigurationProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class ConfigurationProcessor {

    // Instantiate the configuration class interface by proxy
    public static <T extends AutoConfiguration> T process(Class<T> configurationInterface) throws AutoConfigurationException {
        // Check whether the AutoConfiguration interface has configuration annotation
        if (!configurationInterface.isAnnotationPresent(Configuration.class)){
            throw new AutoConfigurationException("AutoConfiguration interface must be decorated with Configuration annotation.");
        }
        return configurationInterface.equals(AutoConfigurationGroup.class)
                ? processConfigurationGroup(configurationInterface)
                : processConfiguration(configurationInterface);
    }

    private static <T extends AutoConfiguration> T processConfigurationGroup(Class<T> configurationInterface){
        // TODO: wait to implements
        return null;
    }

    private static <T extends AutoConfiguration> T processConfiguration(Class<T> configurationInterface){
        ClassLoader classLoader = configurationInterface.getClassLoader();
        InvocationHandler invocationHandler = new ConfigurationProxy<>(configurationInterface);
        return (T) Proxy.newProxyInstance(
                classLoader,
                new Class[]{configurationInterface},
                invocationHandler
        );
    }

}
