package cn.neptunex.configuration.annotations;

import cn.neptunex.configuration.enums.ConfigurationType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface Configuration {

    String value() default "config.yml";
    ConfigurationType type() default ConfigurationType.YAML;
    boolean autoReload() default false;

}
