package cn.neptunex.configuration.proxy;

import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.interfaces.IConfiguration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConfigurationProxy<T extends IConfiguration> implements InvocationHandler {

    private final Class<T> configurationInterface;

    public ConfigurationProxy(Class<T> configurationInterface) {
        this.configurationInterface = configurationInterface;
        if (configurationInterface.isAnnotationPresent(Configuration.class)){
            // TODO:
        }else if (configurationInterface.isAnnotationPresent()){

        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (isGetMethod(methodName)){
            System.out.println(method.getName());
        }
        return null;
    }

    private static boolean isGetMethod(String methodName){
        return methodName.startsWith("get") || methodName.startsWith("is");
    }

    private static boolean isSetMethod(String methodName){
        return methodName.startsWith("is");
    }

}
