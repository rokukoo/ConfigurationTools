package cn.neptunex.configuration.interfaces;

import java.util.List;

public interface AutoConfigurationGroup<T> extends AutoConfiguration {

    List<T> groups();

}
