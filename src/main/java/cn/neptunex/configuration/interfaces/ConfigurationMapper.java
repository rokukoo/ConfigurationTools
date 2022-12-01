package cn.neptunex.configuration.interfaces;

public interface ConfigurationMapper<K extends AutoConfiguration, T> {

    T mapper(K configuration);

}
