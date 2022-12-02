package cn.neptunex.configuration.features;

import java.io.IOException;

public interface AutoConfiguration {

    <T> T get(String path, Class<T> returnType);

    <T> void set(String path, T val);

    void save() throws IOException;

    void reload();

}
