package cn.neptunex.configuration.proxy;

import cn.neptunex.configuration.annotations.Binding;
import cn.neptunex.configuration.annotations.Configuration;
import cn.neptunex.configuration.driver.ConfigurationDriver;
import cn.neptunex.configuration.interfaces.AutoConfiguration;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
public class ConfigurationProxy<T extends AutoConfiguration> implements InvocationHandler {

    private final Class<T> configurationInterface;
    private final Configuration configuration;
    private final String root;
    private final ConfigurationDriver driver;

    public ConfigurationProxy(Class<T> configurationInterface, ConfigurationDriver driver) {
        this.configurationInterface = configurationInterface;
        this.configuration = configurationInterface.getAnnotation(Configuration.class);
        this.driver = driver;
        this.root = configuration.root();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (isSaveMethod(methodName)) {
            driver.save();
            return null;
        }else if (isGetMethod(methodName)){
            return driver.get((String) root + "." + args[0], (Class<?>) args[1]);
        }else if (isSetMethod(methodName)) {
            driver.set((String) args[0], args[1]);
            return null;
        }else if (isGetXMethod(methodName)){
            String path = getPath(method);
            Class<?> returnType = method.getReturnType();
            return driver.get(path, returnType);
        }else if (isSetXMethod(methodName)){
            // TODO: 还没有写设置的代码
            return null;
        }else {
            return method.invoke(proxy, args);
        }
    }

    private String getPath(Method method){
        String path;
        String methodName = method.getName();
        // Judge whether the method is annotated by Binding
        String replace = methodName.replace("get", "").replace("is", "").replace("set", "");
        if (method.isAnnotationPresent(Binding.class)){
            Binding binding = method.getAnnotation(Binding.class);
            String value = binding.value();
            // Judge whether the Binding annotation specifies a path
            if (!"".equals(value)){
                path = value;
            }else{
                path = StringUtils.uncapitalize(replace);
            }
        // If not, use the method name to obtain
        }else{
            path = StringUtils.uncapitalize(replace);
        }
        return root + "." + path;
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
