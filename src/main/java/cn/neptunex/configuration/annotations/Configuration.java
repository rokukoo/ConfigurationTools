package cn.neptunex.configuration.annotations;

import cn.neptunex.configuration.enums.ConfigurationType;
import cn.neptunex.configuration.features.ConfigurationReloadCallback;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface Configuration {

    String value() default "config.yml";
    ConfigurationType type() default ConfigurationType.YAML;
    String root() default "";

    boolean autoReload() default false;
    Class<? extends ConfigurationReloadCallback> reloadCallback() default ConfigurationReloadCallback.class;
    boolean autoSave() default true;

    boolean group() default false;
    String folder() default "";
//    Class<? extends ConfigurationMapper> mapper() default ConfigurationMapper.class;

}
