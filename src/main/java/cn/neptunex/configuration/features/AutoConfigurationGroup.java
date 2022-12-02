package cn.neptunex.configuration.features;

import java.util.List;

public interface AutoConfigurationGroup<T extends AutoConfiguration> extends AutoConfiguration {

    List<T> groups();

}
