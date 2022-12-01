package cn.neptunex.configuration;

import cn.neptunex.configuration.interfaces.IConfiguration;
import cn.neptunex.configuration.proxy.ConfigurationProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class ConfigurationProcessor {

    public static <T extends IConfiguration> List<T> processGroup(Class<T> configurationInterface){
        ClassLoader classLoader = configurationInterface.getClassLoader();
        InvocationHandler invocationHandler = new ConfigurationProxy<>(configurationInterface);
        return null;
    }

    public static <T extends IConfiguration> T process(Class<T> configurationInterface){
        ClassLoader classLoader = configurationInterface.getClassLoader();
        InvocationHandler invocationHandler = new ConfigurationProxy<>(configurationInterface);
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{configurationInterface}, invocationHandler);
    }

}
