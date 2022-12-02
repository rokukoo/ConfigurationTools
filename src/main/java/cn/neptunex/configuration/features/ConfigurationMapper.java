package cn.neptunex.configuration.features;

public interface ConfigurationMapper<K extends AutoConfiguration, T> {

    T mapper(K configuration);

}
