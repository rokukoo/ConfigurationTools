package cn.neptunex.configuration.proxy;

import cn.neptunex.configuration.annotations.Binding;
import cn.neptunex.configuration.driver.ConfigurationDriver;
import cn.neptunex.configuration.interfaces.AutoConfiguration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
public class ConfigurationProxy<T extends AutoConfiguration> implements InvocationHandler {

    private final Class<T> configurationInterface;
    private final ConfigurationDriver driver;

    public ConfigurationProxy(Class<T> configurationInterface, ConfigurationDriver driver) {
        this.configurationInterface = configurationInterface;
        this.driver = driver;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (isSaveMethod(methodName)) {
            driver.save();
            return null;
        }else if (isGetMethod(methodName)){
            return driver.get((String) args[0], (Class<?>) args[1]);
        }else if (isSetMethod(methodName)) {
            driver.set((String) args[0], args[1]);
            return null;
        }else if (isGetXMethod(methodName)){
            Binding binding = method.getAnnotation(Binding.class);
            String path = binding.value();
            Class<?> returnType = method.getReturnType();
            return driver.get(path, returnType);
        }else if (isSetXMethod(methodName)){
            // TODO: 还没有写设置的代码
            return null;
        }else {
            return method.invoke(proxy, args);
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
        return "set".startsWith(methodName);
    }

}
