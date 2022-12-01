package cn.neptunex.configuration.interfaces;

public interface IConfiguration {

    <T> T get(String path, Class<T> returnType);

    <T> void set(String path, T value);

}
