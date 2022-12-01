package cn.neptunex.configuration.proxy;

import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.driver.ConfigurationDriver;
import cn.neptunex.configuration.driver.YamlConfigurationDriver;
import cn.neptunex.configuration.interfaces.AutoConfiguration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ConfigurationProxy<T extends AutoConfiguration> implements InvocationHandler {

    private final Class<T> configurationInterface;
    private final ConfigurationDriver driver;
    // Since hashmap are unordered, policy mode cannot be used here
//    private final Map<Predicate<String>, InvocationHandler> proxyStrategies = new HashMap<>();

    public ConfigurationProxy(Class<T> configurationInterface) {
        this(configurationInterface, chooseDriver(configurationInterface.getAnnotation(Configuration.class)));
    }

    public ConfigurationProxy(Class<T> configurationInterface, ConfigurationDriver driver) {
        this.configurationInterface = configurationInterface;
        this.driver = driver;
//        this.proxyStrategies.put(ConfigurationProxy::isGetMethod, (proxy, method, args) -> driver.get((String) args[0], (Class<?>) args[1]));
//        this.proxyStrategies.put(ConfigurationProxy::isSetMethod, (proxy, method, args) -> driver.get((String) args[0], (Class<?>) args[1]));
//        this.proxyStrategies.put(ConfigurationProxy::isSaveMethod, (proxy, method, args) -> {
//            driver.save();
//            return null;
//        });
//        this.proxyStrategies.put(ConfigurationProxy::is, (proxy, method, args) -> {
//            driver.save();
//            return null;
//        });
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
//        for (Map.Entry<Predicate<String>, InvocationHandler> handlerEntry : proxyStrategies.entrySet()) {
//            if (handlerEntry.getKey().test(methodName)){
//                return handlerEntry.getValue().invoke(proxy, method, args);
//            }
//        }
        if (isSaveMethod(methodName)) {
            driver.save();
            return null;
        }else if (isGetMethod(methodName)){
            return driver.get((String) args[0], (Class<?>) args[1]);
        }else if (isSetMethod(methodName)) {
            driver.set((String) args[0], args[1]);
            return null;
        }else if (isGetXMethod(methodName)){

            return null;
        }else if (isSetXMethod(methodName)){
            return null;
        }else {
            return method.invoke(args);
        }
    }

    private static boolean isSaveMethod(String methodName){
        return "save".equals(methodName);
    }

    private static boolean isGetMethod(String methodName){
        return "get".equals(methodName);
    }

    private static boolean isSetMethod(String methodName){
        return "set".equals(methodName);
    }

    private static boolean isGetXMethod(String methodName){
        return methodName.startsWith("get") || methodName.startsWith("is");
    }

    private static boolean isSetXMethod(String methodName){
        return "is".startsWith(methodName);
    }

    private static ConfigurationDriver chooseDriver(Configuration configurationAnno){
        String fileName = configurationAnno.value();
        // FIXME: 这里到时候肯定还要再改, 因为这里到时候可能会有网络IO, 或者绝对路径读取等等, 我这样写只是测试
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("config.yml");
        assert inputStream != null;
        ConfigurationDriver driver = new YamlConfigurationDriver(new BufferedReader(new InputStreamReader(inputStream)));
        return driver;
    }

}
