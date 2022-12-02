package cn.neptunex.configuration.driver;

import java.io.IOException;

// The structure of this interface is very similar to the AutoConfiguration interface
public interface ConfigurationDriver {

    <T> T get(String path, Class<T> returnType);

    <T> void set(String path, T val);

    void save() throws IOException;

    void reload();

}
