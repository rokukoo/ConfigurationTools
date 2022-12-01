package cn.neptunex.configuration.interfaces;

import java.io.IOException;

public interface AutoConfiguration {

    <T> T get(String path, Class<T> returnType);

    <T> void set(String path, T val);

    void save() throws IOException;

}
